import React, {useEffect, useState} from 'react';
import Header from "../../layout/jsx/Header.jsx";
import {DataGrid} from "@mui/x-data-grid";
import {MEMBER_ADMIN, MEMBER_PROMOTE} from "../../../config/host-config.jsx";
import "../scss/AdminMemberManagement.scss";
import {Button} from "@mui/material";
import axios from "axios";

const AdminMemberManagement = () => {

    const [members, setMembers] = useState([]);
    const [refresh, setRefresh] = useState(false);

    useEffect(() => {
        document.title = "회원관리";
    }, []);

    if (localStorage.getItem("ROLE") !== "ADMIN") {
        alert("잘못된 접근입니다.");
        window.location.href = "/";
    }

    const getAllMembers = async () => {

        const response = await fetch(MEMBER_ADMIN, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${localStorage.getItem("ACCESS_TOKEN")}`,
                "Content-Type": "Application/json",
            }
        });

        if (response.status === 200) {
            const data = await response.json();
            if (Array.isArray(data.members)) {
                setMembers(data.members);
                // console.log(data.members);
            }
        } else if (response.status === 400) {
            const text = await response.text();
            alert(text);
            window.location.href = "/";
        }
    };

    useEffect(() => {
        getAllMembers();
    }, [refresh]);

    const columns = [
        {field: 'email', headerName: '이메일', width: 250},
        {field: 'name', headerName: '이름', width: 100},
        {field: 'reportCount', headerName: '제보 수', width: 100},
        {field: 'joinDate', headerName: '가입날짜', width: 200},
        {field: 'role', headerName: '권한', width: 100},
        {
            field: 'action',
            headerName: 'action',
            width: 200,
            renderCell: (params) => {
                const member = members.find(m => m.email === params.row.email);
                return (
                    <>
                        {member && member.status ? (
                            <Button
                                variant={'contained'}
                                color={'success'}
                                size={"small"}
                                onClick={() => updateMemberState(params.row.email)}
                                style={{marginRight: '8px'}}
                            >
                                탈퇴취소
                            </Button>
                        ) : (
                            <Button
                                variant={'contained'}
                                color={'secondary'}
                                size={"small"}
                                onClick={() => updateMemberState(params.row.email)}
                                style={{marginRight: '8px'}}
                            >
                                강제탈퇴
                            </Button>
                        )}
                        <Button
                            variant={'contained'}
                            color={'primary'}
                            size={"small"}
                            onClick={() => handleChangeRole(params.row.email)}
                        >
                            {params.row.role === '관리자' ? '일반회원으로 변경' : '관리자로 변경'}
                        </Button>
                    </>
                );
            }
        },
        {
            field: 'updatedAt', headerName: "탈퇴처리 일시", width: 200
        }
    ];

    const rows = members.map((member, index) => ({
        id: index,
        email: member.email,
        name: member.userName,
        reportCount: member.reportCount,
        joinDate: new Date(member.joinDate).toLocaleString(),
        role: member.role === "ADMIN" ? "관리자" : "일반회원",
        updatedAt: member.updatedAt !== null ? new Date(member.updatedAt).toLocaleString() : null,
    }));

    /**
     * 관리자 권한으로 유저를 강제탈퇴/탈퇴취소 시키는 함수
     * @param email
     * @returns {Promise<void>}
     */
    const updateMemberState = async (email) => {

        const response = await fetch(MEMBER_ADMIN + "/" + email,
            {
                method: "PATCH",
                headers: {
                    "Authorization": `Bearer ${localStorage.getItem("ACCESS_TOKEN")}`,
                    "Content-Type": "Application/json"
                },
            },
        );

        if (response.status === 200) {
            const text = await response.text();
            alert(text);
            setRefresh(!refresh);
        } else if (response.status === 400) {
            const text = await response.text();
            alert(text);
        }
    };

    const handleChangeRole = async (email) => {

        const response = await fetch(MEMBER_PROMOTE + "/" + email,
            {
                method: "PATCH",
                headers: {
                    "Authorization": `Bearer ${localStorage.getItem("ACCESS_TOKEN")}`,
                    "Content-Type": "Application/json"
                },
            },
        );

        if(response.status === 200) {
            const text = await response.text();
            alert(text);
            setRefresh(!refresh);
        } else if (response.status === 400) {
            const text = await response.text();
            alert(text);
        }
    };

    return (
        <div>
            <Header/>
            <div className={"member-table"}>
                <DataGrid
                    columns={columns}
                    rows={rows}
                    initialState={{
                        pagination: {
                            paginationModel: {page: 0, pageSize: 10},
                        },
                    }}
                />
            </div>
        </div>
    );
};

export default AdminMemberManagement;