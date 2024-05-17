// Modal.js
import React, {useEffect, useState} from 'react';
import '../scss/Modal.scss';
import {REPORT_ADMIN, REPORT_URL} from "../../../config/host-config.jsx";
import {ToggleButton, ToggleButtonGroup} from "@mui/material";

const Modal = ({closeModal, children, category, setShowModal}) => {
    const [inputValue, setInputValue] = useState('');
    const [reportList, setReportList] = useState([]);
    const [filteredReportList, setFilteredReportList] = useState([]);
    const [alignment, setAlignment] = useState('cherry');

    useEffect(() => {
        const getReportList = async () => {
            if (category === "list") {
                const response = await fetch(REPORT_URL, {
                    method: "GET", headers: {
                        "Authorization": `Bearer ${localStorage.getItem("ACCESS_TOKEN")}`,
                        "Content-Type": "Application/json"
                    }
                });

                if (response.status === 200) {
                    const data = await response.json();
                    if (Array.isArray(data.reports)) {
                        setReportList(data.reports);
                        // console.log(data.reports);
                    }
                }
            } else if (category === "admin-report-list") {
                const response = await fetch(REPORT_ADMIN, {
                    method: "GET", headers: {
                        "Authorization": `Bearer ${localStorage.getItem("ACCESS_TOKEN")}`,
                        "Content-Type": "Application/json"
                    }
                });

                if (response.status === 200) {
                    const data = await response.json();
                    if (Array.isArray(data.reports)) {
                        setReportList(data.reports);
                        // console.log(data.reports);
                    }
                }
            } else {
                return;
            }


        };

        getReportList();
    }, [category]);

    useEffect(() => {
        setFilteredReportList(reportList.filter(report => report.category === alignment.toUpperCase()));
    }, [alignment, reportList]);

    const handleInputChange = (e) => {
        setInputValue(e.target.value);
    };

    const postReports = async () => {
        if (inputValue === '') {
            return;
        }

        const response = await fetch(REPORT_URL, {
            method: "POST", body: JSON.stringify({category: category.toUpperCase(), content: inputValue}), headers: {
                "Authorization": `Bearer ${localStorage.getItem("ACCESS_TOKEN")}`, "Content-Type": "Application/json"
            }
        });

        if (response.status === 200) {
            alert("성공적으로 제보되었습니다!");
            setShowModal(false);
        }
    }

    const handleChange = (event, newAlignment) => {
        setAlignment(newAlignment);
    };

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        return `${year}-${month}-${day} ${hours}:${minutes}`;
    };

    return (
        <div className="modal-overlay" onClick={closeModal}>
        <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <button className="modal-close" onClick={closeModal}>
                <i className="fas fa-times"></i>
            </button>
            <div className="modal-header">
                <h2>{children}</h2>
            </div>
            {category === "trash" || category === "cherry" ?
                <div className="modal-body">
                <div className="modal-input">
                        <textarea
                            className={"report-textfield"}
                            value={inputValue}
                            onChange={handleInputChange}
                            placeholder={category === "trash" ? `OO구 OO로/길 OO근처, 쓰레기통 종류\nex) 강남구 영동대로 513 코엑스 정문 일반쓰레기` : "OO/OO 한강공원 or 한강공원"}
                        />
                </div>
            </div>
                : category === "list" ?
                <div className={"report-list"}>
                    <ToggleButtonGroup
                        color={"primary"}
                        value={alignment}
                        exclusive
                        onChange={handleChange}
                        aria-label={"Category"}
                    >
                        <ToggleButton value={"cherry"}>벚꽃</ToggleButton>
                        <ToggleButton value={"trash"}>쓰레기통</ToggleButton>
                    </ToggleButtonGroup>
                    <table>
                        <thead>
                        <tr>
                            <th>내용</th>
                            <th>제보일자</th>
                            <th>처리상태</th>
                        </tr>
                        </thead>
                        <tbody>
                        {filteredReportList.map((report, index) => (<tr key={index}>
                            <td>{report.content}</td>
                            <td>{formatDate(report.reportDate)}</td>
                            <td>{report.status ? "처리완료" : "대기중"}</td>
                        </tr>))}
                        </tbody>
                    </table>
                </div>
                : <></>
            }
            <div className="modal-footer">
                {(category === "trash" || category === "cherry") && (
                    <button className="modal-button" onClick={postReports}>제출</button>
                )}
                {
                    category === "list" && <span style={{color: "blue"}}> * 처리완료된 제보는 일주일 뒤 자동으로 삭제됩니다.</span>
                }
            </div>
        </div>
    </div>);
};

export default Modal;