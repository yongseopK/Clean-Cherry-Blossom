import React, {useEffect} from 'react';
import {useLocation, useNavigate} from "react-router-dom";
import axios from "axios";
import {ROLE, TOKEN, USERNAME} from "../../../util/login-util.jsx";
import {NAVER_OAUTH_API_URL} from "../../../config/host-config.jsx";

const NaverLogin = () => {

    const location = useLocation();
    const getUserInfo = async () => {
        if (!location.hash) return;

        const token = location.hash.split('=')[1].split('&')[0];

        const response = await axios.post(NAVER_OAUTH_API_URL, {
            token: token,
        }, {withCredential: true})


        if (response.status === 200) {
            const {token, userName, role} = await response.data;
            localStorage.setItem(TOKEN, token);
            localStorage.setItem(USERNAME, userName);
            localStorage.setItem(ROLE, role);
            window.close();
            window.opener.location.replace('/');
        }
    }

    useEffect(() => {
        getUserInfo();
    }, []);

    return (
        <></>
    );
};

export default NaverLogin;