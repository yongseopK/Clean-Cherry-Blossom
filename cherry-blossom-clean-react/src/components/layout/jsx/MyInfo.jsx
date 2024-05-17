import React, {useEffect, useState} from 'react';
import Header from "./Header.jsx";
import {getLogin} from "../../../util/login-util.jsx";
import "../scss/MyInfo.scss";
import {INFO_URL, WITHDRAWAL_URL} from "../../../config/host-config.jsx";
import {Button, CircularProgress, Paper, TextField} from "@mui/material";
import Modal from 'react-modal';
import '../scss/WithdrawalModal.scss';

const MyInfo = () => {

    const [info, setInfo] = useState({});
    const [isLoading, setIsLoading] = useState(false);
    const [nameValue, setNameValue] = useState('');
    const [passwordValue, setPasswordValue] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');

    const [isModalOpen, setIsModalOpen] = useState(false);

    const [nameValid, setNameValid] = useState(false);
    const [passwordValid, setPasswordValid] = useState(false);

    const [nameError, setNameError] = useState('');
    const [passwordError, setPasswordError] = useState('');
    const [confirmPasswordError, setConfirmPasswordError] = useState('');


    const openModal = () => {
        setIsModalOpen(true);
    }

    const closeModal = () => {
        setIsModalOpen(false);
    };

    const fetchMyInfo = async () => {

        setIsLoading(true);

        const response = await fetch(INFO_URL,
            {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${localStorage.getItem("ACCESS_TOKEN")}`,
                    "Content-Type": "Applicaion/json"
                }
            });
        if (response.status === 200) {
            const data = await response.json();
            setInfo(data);
            setIsLoading(false);
        }
    };

    const nameHandler = (e) => {
        if (e.target.value === "") {
            setNameValid(false);
            setNameError('');
        } else {
            const nameRegex = /^[가-힣]{2,4}$/;
            const isNameValid = nameRegex.test(e.target.value);
            setNameValid(isNameValid);
            setNameError(isNameValid ? '' : '이름은 2~4자의 한글로 입력해주세요.');
        }
        setNameValue(e.target.value);
    };


    const passwordHandler = (e) => {
        if (e.target.value === "") {
            setPasswordValid(false);
            setPasswordError('');
        } else {
            const passwordRegex = /^(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$/;
            const isPasswordValid = passwordRegex.test(e.target.value);
            setPasswordValid(isPasswordValid && e.target.value === confirmPassword);
            setPasswordError(isPasswordValid ? '' : '비밀번호는 8자 이상이며, 특수문자를 포함해야 합니다.');
        }
        setPasswordValue(e.target.value);
    };

    const confirmPasswordHandler = (e) => {
        if (e.target.value === "") {
            setPasswordValid(false);
            setConfirmPasswordError('');
        } else {
            const isConfirmValid = e.target.value === passwordValue;
            setPasswordValid(isConfirmValid && /^(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$/.test(passwordValue));
            setConfirmPasswordError(isConfirmValid ? '' : '비밀번호가 일치하지 않습니다.');
        }
        setConfirmPassword(e.target.value);
    };

    const resetInput = () => {
        setNameValue('');
        setNameValid(false);
        setNameError('');
        setPasswordValue('');
        setPasswordValid(false);
        setPasswordError('')
        setConfirmPassword('');
        setConfirmPasswordError('');
    };

    const handleWithdrawal = async (e) => {

        const response = await fetch(WITHDRAWAL_URL,
            {
                method: "POST",
                headers: {
                    "Authorization": `Bearer ${localStorage.getItem("ACCESS_TOKEN")}`,
                    "Content-Type": "Application/json"
                }
            });

        if (response.status === 200) {
            const text = await response.text();
            alert(text);
            localStorage.clear();
            closeModal();
            window.location.href = "/"
        } else if (response.status === 400) {
            const text = await response.text();
            alert(text);

        }


    };

    const handleModifyInfo = async () => {
        const modifyData = {};

        if (nameValue !== "") {
            modifyData.userName = nameValue;
        }

        if (passwordValue !== "") {
            modifyData.password = passwordValue;
        }

        try {
            const response = await fetch("http://localhost:8888/api/members/modify", {
                method: "PUT",
                headers: {
                    "Authorization": `Bearer ${localStorage.getItem("ACCESS_TOKEN")}`,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(modifyData)
            });

            if (response.ok) {
                alert("회원 정보가 수정되었습니다.");
                setNameValue('');
                setPasswordValue('');
                setConfirmPassword('');
                fetchMyInfo();
            } else {
                const errorText = await response.text();
                alert(`회원 정보 수정에 실패했습니다: ${errorText}`);
            }
        } catch (error) {
            console.error("Error:", error);
            alert("회원 정보 수정 중 오류가 발생했습니다.");
        }
    };

    useEffect(() => {
        document.title = "내 정보";
        fetchMyInfo();
    }, []);

    if (!getLogin()) {
        alert("로그인 후 이용할 수 있습니다.");
        window.location.href = "/";
    }

    if (isLoading) {
        return <><Header/>
            <div style={{height: '100vh', display: 'flex', justifyContent: 'center', alignItems: 'center'}}>
                <CircularProgress/></div>
        </>
    }

    return (
        <div className={"info-component"}>
            <Header/>
            <div className="info-container" style={{flex: 1}}>
                <Paper elevation={3} className="info-form">
                    {
                        info.platform !== "LOCAL" &&
                        <img src={info.platform === "GOOGLE" ? `images/googleLogin.png` : `images/naverlogo.png`}
                             alt={info.platform === "GOOGLE" ? "구글" : "네이버"}
                             title={info.platform === "GOOGLE" ? "구글" : "네이버"} className={"sns-login-badge"}/>
                    }

                    <div className="email-container info-div">
                        <span className="info-des">이메일</span>
                        <input type="text" disabled value={info.email}/>
                    </div>
                    <div className="name-container info-div">
                        <span className="info-des">이름</span>
                        <input type="text" placeholder={info.userName} value={nameValue} onChange={nameHandler} disabled={info.platform !== "LOCAL"}/>
                        {nameError && <span className="error-message">{nameError}</span>}
                    </div>
                    {
                        info.platform === "LOCAL" &&
                        <div className="password-container info-div">
                            <span className="info-des">비밀번호</span>
                            <input type="password" placeholder={"비밀번호"} value={passwordValue}
                                   onChange={passwordHandler}/>
                            {passwordError && <span className="error-message">{passwordError}</span>
                            }
                        </div>
                    }
                    {
                        info.platform === "LOCAL" &&
                        <div className="confirm-password-container info-div">
                            <span className="info-des">비밀번호 확인</span>
                            <input type="password" placeholder={"비밀번호 확인"} value={confirmPassword}
                                   onChange={confirmPasswordHandler}/>
                            {confirmPasswordError && <span className="error-message">{confirmPasswordError}</span>}
                        </div>
                    }
                    <div className="role-container info-div">
                        <span className="info-des">권한 </span>
                        <input type="text" disabled placeholder={info.role === "COMMON" ? "일반회원" : "관리자"}/>
                    </div>

                    {
                        info.platform === "LOCAL" ?
                        <div className="button-group">
                        <Button
                            variant={"outlined"}
                            color={"success"}
                            onClick={handleModifyInfo}
                            size={"large"}
                            disabled={!nameValid && !passwordValid}
                        >
                            정보수정
                        </Button>
                        <Button variant={"outlined"} color={"error"} size={"large"} onClick={resetInput}>되돌리기</Button>
                    </div>
                    :
                            <span className={"sns-user-description"}>SNS가입 회원은 정보를 수정할 수 없습니다.</span>
                    }
                    <div className="withdrawal">
                        <p onClick={openModal}>회원탈퇴</p>
                    </div>
                    <Modal
                        isOpen={isModalOpen}
                        onRequestClose={closeModal}
                        contentLabel="회원 탈퇴 모달"
                        className="withdrawal-modal"
                        overlayClassName="withdrawal-modal-overlay"
                    >
                        <h2>회원 탈퇴 안내</h2>
                        <p>회원 탈퇴를 진행하시면 다음과 같은 사항이 적용됩니다:</p>
                        <ul>
                            <li> - 회원님의 계정 정보가 삭제됩니다.</li>
                            <li> - 회원님의 게시물, 댓글 등의 데이터가 삭제됩니다.</li>
                            <li> - 탈퇴 후 7일 동안은 회원 탈퇴를 취소할 수 있습니다.</li>
                            <li> - 7일 이후에는 회원 정보가 완전히 삭제되어 복구할 수 없습니다.</li>
                        </ul>
                        <p>정말로 회원 탈퇴를 진행하시겠습니까?</p>
                        <div className="modal-buttons">
                            <button onClick={handleWithdrawal}>회원 탈퇴</button>
                            <button onClick={closeModal}>취소</button>
                        </div>
                    </Modal>
                </Paper>
            </div>
        </div>
    );
};

export default MyInfo;