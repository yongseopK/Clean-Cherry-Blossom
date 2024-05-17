import React, {useEffect, useState} from 'react';
import {REPORT_ADMIN} from "../../../config/host-config.jsx";
import "../scss/AdminReportList.scss";
import {DataGrid} from '@mui/x-data-grid';
import Header from "../../layout/jsx/Header.jsx";
import {Button} from "@mui/material";

const AdminReportList = () => {

    const [reportList, setReportList] = useState([]);
    const [refresh, setRefresh] = useState(false);

    useEffect(() => {
        document.title = "제보목록";
    }, []);

    const getAllReportList = async () => {

        const response = await fetch(REPORT_ADMIN, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${localStorage.getItem("ACCESS_TOKEN")}`,
                "Content-Type": "Application/json",
            }
        });

        if (response.status === 200) {
            const data = await response.json();
            if (Array.isArray(data.reports)) {
                setReportList(data.reports);
                // console.log(data.reports);
            }
        } else if (response.status === 400) {
            const text = await response.text();
            alert(text);
            window.location.href = "/";
        }
    };

    useEffect(() => {
        getAllReportList();
    }, [refresh]);

    const rows = reportList.map((report, index) => ({
        id: report.reportId,
        category: report.category === "CHERRY" ? "벚꽃" : "쓰레기통",
        content: report.content,
        email: report.email,
        date: new Date(report.reportDate).toLocaleString(),
        updatedAt: report.updatedAt !== null ? new Date(report.updatedAt).toLocaleString() : null,
    }));

    const columns = [
        {field: 'category', headerName: "카테고리", width: 130},
        {field: 'content', headerName: "내용", width: 250},
        {field: 'email', headerName: "이메일", width: 250},
        {field: 'date', headerName: "제보날짜", width: 200},
        {
            field: 'actions',
            headerName: '상태',
            width: 100,
            renderCell: (params) => {
                const report = reportList.find(r => r.reportId === params.row.id);
                if (report && report.status) {
                    return <span style={{color: "blue"}}>처리완료</span>;
                } else {
                    return (
                        <Button
                            variant={"contained"}
                            color={"secondary"}
                            size={"small"}
                            onClick={() => handleRemoveReport(params.row.id)}
                        >
                            처리하기
                        </Button>
                    )
                }
            }
        },
        {
            field: 'updatedAt', headerName: "처리날짜", width: 200
        }
    ];

    const handleRemoveReport = async (reportId) => {
        // console.log(reportId);

        const response = await fetch(REPORT_ADMIN + "/" + reportId, {
            method: "PATCH",
            headers: {
                "Authorization": `Bearer ${localStorage.getItem("ACCESS_TOKEN")}`
            }
        })

        if (response.status === 200) {
            const text = await response.text();
            alert(text);
            setRefresh(!refresh)
        } else if (response.status === 400) {
            const text = await response.text();
            alert(text);
        }
    };


    const [sortModel, setSortModel] = useState([
        {
            field: 'date',
            sort: 'desc',
        },
    ]);

    if (localStorage.getItem("ROLE") !== "ADMIN") {
        alert("잘못된 접근입니다.");
        window.location.href = "/";
    }

    return (
        <div>
            <Header/>

            <div className={"report-table"}>
                <DataGrid
                    columns={columns}
                    rows={rows}
                    initialState={{
                        pagination: {
                            paginationModel: {page: 0, pageSize: 10},
                        },
                    }}
                    sortModel={sortModel}
                    onSortModelChange={(model) => setSortModel(model)}
                />
            </div>
        </div>
    );
};

export default AdminReportList;