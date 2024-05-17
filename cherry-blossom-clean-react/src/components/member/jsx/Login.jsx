import React, {useEffect, useState} from 'react';
import {Link, useNavigate} from "react-router-dom";
import axios from "axios";
import {API_BASE_URL, LOGIN_URL, WITHDRAWAL_CANCEL_URL} from "../../../config/host-config.jsx";
import "../scss/Login.scss";
import {getLogin, ROLE, TOKEN, USERNAME} from "../../../util/login-util.jsx";
import {Main} from "../../../App.jsx";
import GoogleLoginComponent from "./GoogleLoginComponent.jsx";
import NaverLoginComponent from "./NaverLoginComponent.jsx";
import ForgetPassword from "./ForgetPassword.jsx";

const Login = () => {

    const redirection = useNavigate();

    const [autoLogin, setAutoLogin] = useState(false);

    const [showPopup, setShowPopup] = useState(false);

    const openPopup = () =>{
        setShowPopup(true);
    }

    const closePopup = () => {
        setShowPopup(false);
    }

    useEffect(() => {
        document.title = '로그인';
    }, []);

    const enterHandler = async (e) => {
        if (e.code === "Enter") {
            await fetchLoginProcess();
        }
    };

    const fetchLoginProcess = async () => {
        const emailInput = document.getElementById('email');
        const passwordInput = document.getElementById('password');

        if (emailInput.value === "" || passwordInput.value === "") {
            alert("정보를 입력해주세요");
            return;
        }

        try {
            const response = await axios.post(
                LOGIN_URL,
                JSON.stringify({
                    email: emailInput.value,
                    password: passwordInput.value,
                    autoLogin : autoLogin,
                }),
                {headers: {"Content-Type": "Application/json"}},
            );

            if (response.status === 200) {
                const {token, userName, role} = await response.data;
                localStorage.setItem(TOKEN, token);
                localStorage.setItem(USERNAME, userName);
                localStorage.setItem(ROLE, role);
                redirection('/');
            }
        } catch (err) {
            if (err.response) {
                const errorMessage = err.response.data;
                if (err.response.status === 400) {
                    if (errorMessage === "회원 탈퇴 요청 상태입니다. 7일 이내에 로그인하시면 탈퇴 취소가 가능합니다.") {
                        const confirmWithdrawalCancel = window.confirm(errorMessage);
                        if (confirmWithdrawalCancel) {
                            try {
                                const response = await axios.post(
                                    WITHDRAWAL_CANCEL_URL,
                                    JSON.stringify({
                                        email: emailInput.value,
                                        password: passwordInput.value,
                                    }),
                                    {
                                        headers: {
                                            "Content-Type": "Application/json",
                                        },
                                    },
                                );

                                if (response.status === 200) {
                                    const {token, userName, role} = response.data;
                                    localStorage.setItem(TOKEN, token);
                                    localStorage.setItem(USERNAME, userName);
                                    localStorage.setItem(ROLE, role);
                                    alert("탈퇴 취소가 완료되었습니다. 로그인 되었습니다.");
                                    redirection('/');
                                }
                            } catch (error) {
                                console.error("탈퇴 취소 에러:", error);
                                if (error.response && error.response.status === 400) {
                                    alert(error.response.data);
                                } else {
                                    alert("탈퇴 취소 중 오류가 발생했습니다.");
                                }
                            }
                        }
                    } else {
                        alert(errorMessage);
                    }
                } else if (err.response.status === 401) {
                    alert("아이디 또는 비밀번호가 잘못되었습니다.");
                } else if(err.response.status === 450) {
                    alert(errorMessage);
                } else {
                    alert("로그인 중 오류가 발생했습니다.");
                }
            } else {
                console.error("로그인 에러:", err);
                alert("로그인 중 오류가 발생했습니다.");
            }
        }
    };

    if (getLogin()) {
        window.location.href = "/";
    }

    const googleLoginHandler = async () => {
        redirection("")
    };

    const autoLoginHandler = (e) => {
        setAutoLogin(!autoLogin);
    };


    return (
        <div className={"login-component"}>
            <div className={"member-background"}></div>
            <div className="login-form">
                <span className={"login-title"}>로그인</span>
                <div className={"input-area"}>
                    <input id={"email"} className={"login-input"} type="text" placeholder={"이메일"}
                           onKeyUp={enterHandler}/>
                    <input id={"password"} className={"login-input"} type="password" placeholder={"비밀번호"}
                           onKeyUp={enterHandler}/>
                </div>
                <div className="auto-login-forgot-password">
                    <label className={"auto-login-label"}>
                        <input type="checkbox" id={"auto-login-checkbox"} onChange={autoLoginHandler}/>
                        자동 로그인
                    </label>
                    <p className={"forgot-password"} onClick={openPopup}>비밀번호를 잊으셨나요?</p>
                    <ForgetPassword isOpen={showPopup} onClose={closePopup}/>
                </div>
                <button className={"btn-login-process"} onClick={fetchLoginProcess}>Login</button>
                <span className={"intend-register"}>가입된 회원이 아니신가요? <Link to={"/register"}>회원가입</Link></span>
                <div className={"social-login-title"}>
                    <div></div>
                    <span>간단로그인</span>
                    <div></div>
                </div>

                <div className="social-login">
                    {/*<img src={'images/googleLogin.png'} alt="google login" title={"구글 로그인"} onClick={googleLoginHandler}/>*/}
                    {/*<img src={'images/kakaoLogin.png'} alt="google login" title={"카카오 로그인"}/>*/}
                    <GoogleLoginComponent />
                    <NaverLoginComponent/>
                </div>


            </div>
        </div>


    );
};

export default Login;