import React, {useEffect} from "react";
import MapTemplate from "./components/blossom-map/jsx/MapTemplate.jsx";
import {NavermapsProvider} from "react-naver-maps";
import {NAVER_MAP_CLIENT_ID} from "./config/host-config.jsx";
import {Container as MapDiv, NaverMap, Marker, useNavermaps} from 'react-naver-maps'
import Header from "./components/layout/jsx/Header.jsx";
import {Route, Routes} from "react-router-dom";
import Login from "./components/member/jsx/Login.jsx";
import Register from "./components/member/jsx/Resister.jsx";
import AdminReportList from "./components/blossom-map/jsx/AdminReportList.jsx";
import AdminMemberManagement from "./components/blossom-map/jsx/AdminMemberManagement.jsx";
import MyInfo from "./components/layout/jsx/MyInfo.jsx";
import ErrorPage from "./components/layout/jsx/ErrorPage.jsx";
import NaverLogin from "./components/member/jsx/NaverLogin.jsx";
import ForgetPassword from "./components/member/jsx/ForgetPassword.jsx";

function App() {

    return (
        <Routes>
            <Route path={"/"} element={<Main/>}/>
            <Route path={"/register"} element={<Register/>}/>
            <Route path={"/login"} element={<Login/>}/>
            <Route path={"/admin-report-list"} element={<AdminReportList/>}/>
            <Route path={"/admin-member-management"} element={<AdminMemberManagement/>}/>
            <Route path={"/my-info"} element={<MyInfo />}></Route>
            <Route path={"/naverLogin"} element={<NaverLogin/>}/>
            <Route path={"/forget-password"} element={<ForgetPassword/>}/>
            <Route path="*" element={<ErrorPage />} />
        </Routes>

    )
}

export const Main = () => {

    useEffect(() => {
        document.title = '메인 페이지';

    }, []);
    return (
        <NavermapsProvider
            ncpClientId={NAVER_MAP_CLIENT_ID}
        >
            <Header/>
            <MapDiv
                style={{
                    width: '100%',
                    height: '70vh',
                }}
            >
                <MapTemplate/>
            </MapDiv>
        </NavermapsProvider>
    );
}

export default App
