import React, {useEffect, useState} from 'react';
import {NAVER_LOGIN_KEY} from "../../../config/host-config.jsx";
import {useLocation} from "react-router-dom";

const NaverLoginComponent = () => {

    const {naver} = window;

    const initializeNaverLogin = () => {
        const naverLogin = new naver.LoginWithNaverId({
            clientId: NAVER_LOGIN_KEY,
            callbackUrl: "http://localhost:3000/naverLogin",
            isPopup: true,
            loginButton: {
                color: "green",
                type: 1,
                height: 40,
            }
        });
        naverLogin.init();

    };

    const location = useLocation();

    const getNaverToken = () => {
        if (!location.hash) return;

        const token = location.hash.split("=")[1].split('&')[0];
        // console.log(token)
    };

    useEffect(() => {
        initializeNaverLogin();
        getNaverToken();
    }, []);

    return (
        <>
            <div id="naverIdLogin"></div>
        </>
    );
};

export default NaverLoginComponent;