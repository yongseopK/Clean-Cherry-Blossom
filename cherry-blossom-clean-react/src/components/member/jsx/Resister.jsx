import React, {useEffect, useState} from 'react';
import '../scss/Register.scss';
import {CHECK_EMAIL, REGISTER_URL} from "../../../config/host-config.jsx";
import {json, useNavigate, useNavigation} from "react-router-dom";
import {getLogin} from "../../../util/login-util.jsx";
import {Main} from "../../../App.jsx";

const Resister = () => {

    const redirection = useNavigate();


    useEffect(() => {
        document.title = '회원가입';
    }, []);

    const [inputStates, setInputStates] = useState({
        email: {value: '', message: '', isValid: false},
        name: {value: '', message: '', isValid: false},
        password: {value: '', message: '', isValid: false},
        passwordConfirm: {value: '', message: '', isValid: false},
    });

    const validateInput = async (fieldName, inputVal) => {
        let msg = '';
        let valid = false;

        // 각 입력 필드에 대한 유효성 검사 로직을 구현합니다.
        if (fieldName === 'email') {
            const emailRegex = /^[a-z0-9\.\-_]+@([a-z0-9\-]+\.)+[a-z]{2,6}$/;

            if (!inputVal) {
                msg = '이메일은 필수값입니다.';
            } else if (!emailRegex.test(inputVal)) {
                msg = '이메일 형식이 아닙니다.';
            } else {
                const response = await fetch(CHECK_EMAIL + '?email=' + inputVal);
                const json = await response.json();
                valid = !json;
                msg = json ? '중복된 이메일입니다.' : '사용 가능한 이메일입니다.';
            }
        } else if (fieldName === 'name') {
            const nameRegex = /^[가-힣]{2,5}$/;

            if (!inputVal) {
                msg = "이름을 입력해주세요";
                valid = false;
            } else if (!nameRegex.test(inputVal)) {
                msg = "이름은 2~5자의 한글로 입력해주세요.";
                valid = false;
            } else {
                msg = "사용 가능한 이름입니다.";
                valid = true;
            }
        } else if (fieldName === 'password') {
            const pwRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,20}$/;

            if (!inputVal) {
                msg = '비밀번호는 필수값입니다.';
            } else if (!pwRegex.test(inputVal)) {
                msg = '8~20자의 영문, 숫자, 특수문자를 포함한 비밀번호를 입력해주세요.';
            } else {
                msg = '사용 가능한 비밀번호입니다.';
                valid = true;
            }
        } else if (fieldName === 'passwordConfirm') {
            if (!inputVal) {
                msg = '비밀번호 확인은 필수값입니다.';
            } else if (inputVal !== inputStates.password.value) {
                msg = '비밀번호가 일치하지 않습니다.';
            } else {
                msg = '비밀번호가 일치합니다.';
                valid = true;
            }
        }


        setInputStates((prevState) => ({
            ...prevState,
            [fieldName]: {value: inputVal, message: msg, isValid: valid},
        }));
    };

    const handleInputChange = (e) => {
        const {name, value} = e.target;
        validateInput(name, value);
    };

    const isAllValid = Object.values(inputStates).every((field) => field.isValid);

    const registerHandler = async (e) => {
        const userData = {
            "email" : inputStates.email.value,
            "userName" : inputStates.name.value,
            "password" : inputStates.password.value,
        }

        const response = await fetch(
            REGISTER_URL,
            {
                method : "POST",
                body : JSON.stringify(userData),
                headers : {
                    "Content-Type" : "Application/json",
                }
            }
        )

        if(response.status === 200) {
            alert("회원가입에 성공했습니다!");
            redirection("/login");
        } else if (response.status === 400) {
            alert("요청이 잘못되었습니다.");
        } else if (response.status === 500) {
            alert("서버 오류가 발생했습니다.");
        } else {
            alert("알 수 없는 오류가 발생했습니다.");
        }
    };

    if(getLogin()) {
        window.location.href = "/";
    }

    return (
        <div className={"register-component"}>
            <div className="register-background"></div>
            <div className="register-form">
                <span className="register-title">회원가입</span>
                <div className="register-input-area">
                    <div className="register-input-container">
                        <input type="email" name="email" placeholder={"이메일"} value={inputStates.email.value}
                               onChange={handleInputChange}/>
                        <span
                            className={`validated-message ${inputStates.email.isValid ? 'green' : 'red'}`}>{inputStates.email.message}</span>
                    </div>
                    <div className="register-input-container">
                        <input type="text" name="name" placeholder={"이름"} value={inputStates.name.value}
                               onChange={handleInputChange}/>
                        <span
                            className={`validated-message ${inputStates.name.isValid ? 'green' : 'red'}`}>{inputStates.name.message}</span>
                    </div>
                    <div className="register-input-container">
                        <input type="password" name="password" placeholder={"비밀번호"} value={inputStates.password.value}
                               onChange={handleInputChange}/>
                        <span
                            className={`validated-message ${inputStates.password.isValid ? 'green' : 'red'}`}>{inputStates.password.message}</span>
                    </div>
                    <div className="register-input-container">
                        <input type="password" name="passwordConfirm" placeholder={"비밀번호 확인"}
                               value={inputStates.passwordConfirm.value} onChange={handleInputChange}/>
                        <span
                            className={`validated-message ${inputStates.passwordConfirm.isValid ? 'green' : 'red'}`}>{inputStates.passwordConfirm.message}</span>
                    </div>
                </div>
                <button className={`btn-register-process ${!isAllValid ? "disabled" : ""}`} disabled={!isAllValid} onClick={registerHandler}>회원가입</button>
            </div>
        </div>
    );
};

export default Resister;