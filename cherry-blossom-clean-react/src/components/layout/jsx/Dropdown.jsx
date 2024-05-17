import React, {useState, useEffect, useRef} from 'react';
import '../scss/Dropdown.scss';
import {IoMenu} from 'react-icons/io5';
import {Link, useNavigate} from "react-router-dom";
import Modal from './Modal.jsx';
import AdminReportList from "../../blossom-map/jsx/AdminReportList.jsx";

const Dropdown = ({login}) => {
    const [isOpen, setIsOpen] = useState(false);
    const [showModal, setShowModal] = useState(false);
    const [modalContent, setModalContent] = useState('');
    const [modalCategory, setModalCategory] = useState('');
    const dropdownRef = useRef(null);

    const redirection = useNavigate();

    const toggleDropdown = () => {
        setIsOpen(!isOpen);
    };

    const handleOutsideClick = (event) => {
        if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
            setIsOpen(false);
        }
    };

    useEffect(() => {
        document.addEventListener('mousedown', handleOutsideClick);

        return () => {
            document.removeEventListener('mousedown', handleOutsideClick);
        };
    }, []);

    const logoutHandler = () => {
        localStorage.clear();
        window.location.reload();
    };

    const openModal = (content, category) => {
        setModalContent(content);
        setModalCategory(category)
        setShowModal(true);
    };

    const closeModal = () => {
        setShowModal(false);
    };

    return (
        <div className="dropdown" ref={dropdownRef}>
            <IoMenu className="menu-btn" onClick={toggleDropdown}/>
            <ul className={`dropdown-menu ${isOpen ? 'active' : ''}`}>

                {
                    localStorage.getItem("ROLE") === "ADMIN" ? (
                            <>
                                <li><Link to={"/admin-report-list"}>전체 제보 목록</Link></li>
                                <li><Link to={"/admin-member-management"}>회원 관리</Link></li>
                            </>

                        )
                        : <>
                            <li onClick={() => openModal('벚꽃 명소/개화시기 제보', 'cherry')}>벚꽃 명소/개화시기 제보</li>
                            <li onClick={() => openModal('쓰레기통 위치 제보', 'trash')}>쓰레기통 위치 제보</li>
                            <li onClick={() => openModal('나의 제보내역', 'list')}>나의 제보내역</li>
                            <li><Link to={"/my-info"}>내 정보</Link></li>
                        </>
                }
                <li onClick={logoutHandler}>로그아웃</li>
            </ul>

            {
                showModal && (
                    <Modal closeModal={closeModal} category={modalCategory} setShowModal={setShowModal}>
                        <h2>{modalContent}</h2>
                    </Modal>
                )
            }
        </div>
    );
};

export default Dropdown;