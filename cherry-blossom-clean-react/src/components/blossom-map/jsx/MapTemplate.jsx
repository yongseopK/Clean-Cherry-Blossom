import React, {useEffect, useRef, useState} from 'react';
import {NaverMap, Marker, useNavermaps, Polygon, InfoWindow} from 'react-naver-maps';
import '../scss/MapTemplate.scss';
import {FaTrash} from 'react-icons/fa';
import {IoMdFlower} from 'react-icons/io';
import Skeleton from '../../layout/jsx/Skeleton.jsx';
import seoulBoundaryData from '../../../assets/boundary/seoulBoundary.json';
import incheonBoundaryData from '../../../assets/boundary/incheonBoundary.json';
import seosanBoundaryData from '../../../assets/boundary/seosanBoundary.json';
import daejeonBoundaryData from '../../../assets/boundary/daejeonBoundary.json';
import gwangjuBoundaryData from '../../../assets/boundary/gwangjuBoundary.json';
import mokpoBoundaryData from '../../../assets/boundary/mokpoBoundary.json';
import chuncheonBoundaryData from '../../../assets/boundary/chuncheonBoundary.json';
import gangneungBoundaryData from '../../../assets/boundary/gangneungBoundary.json';
import andongBoundaryData from '../../../assets/boundary/andongBoundary.json';
import daeguBoundaryData from '../../../assets/boundary/daeguBoundary.json';
import ulsanBoundaryData from '../../../assets/boundary/ulsanBoundary.json';
import busanBoundaryData from '../../../assets/boundary/busanBoundary.json';
import yeosuBoundaryData from '../../../assets/boundary/yeosuBoundary.json';
import seogwipoBoundaryData from '../../../assets/boundary/seogwipoBoundary.json';
import CustomOverlay from "./CustomOverlay.jsx";
import {TOKEN_VALIDATE_API_URL, TRASH_CAN_LOCATION} from "../../../config/host-config.jsx";


const MapTemplate = () => {
    const navermaps = useNavermaps();
    const [currentPosition, setCurrentPosition] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    const [isTrashCanOpen, setIsTrashCanOpen] = useState(false);
    const [isCherryBlossomOpen, setIsCherryBlossomOpen] = useState(false);
    const trashCanDropdownRef = useRef(null);
    const cherryBlossomDropdownRef = useRef(null);
    const [markers, setMarkers] = useState([]);
    const [checkedLocations, setCheckedLocations] = useState([]);
    const [infoWindows, setInfoWindows] = useState([]);
    const [selectedLocations, setSelectedLocations] = useState({});
    const mapRef = useRef(null);

    const trashCanLocation = [
        {name: '관악구', value: 'trash-can-gwanak'},
        {name: '동작구', value: 'trash-can-dongjak'},
        {name: '영등포구', value: 'trash-can-yeongdeungpo'},
        {name: '용산구', value: 'trash-can-yongsan'},
    ];

    const cherryblossomLocation = [
        {name: '서울특별시', value: 'cherry-blossom-seoul', coords: seoulBoundaryData, date: '4/3'},
        {name: '인천광역시', value: 'cherry-blossom-incheon', coords: incheonBoundaryData, date: '4/7'},
        {name: '서산시', value: 'cherry-blossom-seosan', coords: seosanBoundaryData, date: '4/6'},
        {name: '대전광역시', value: 'cherry-blossom-daejeon', coords: daejeonBoundaryData, date: '3/30'},
        {name: '광주광역시', value: 'cherry-blossom-gwangju', coords: gwangjuBoundaryData, date: '3/28'},
        {name: '목포시', value: 'cherry-blossom-mokpo', coords: mokpoBoundaryData, date: '3/29'},
        {name: '춘천시', value: 'cherry-blossom-chuncheon', coords: chuncheonBoundaryData, date: '4/7'},
        {name: '강릉시', value: 'cherry-blossom-gangneung', coords: gangneungBoundaryData, date: '3/31'},
        {name: '안동시', value: 'cherry-blossom-andong', coords: andongBoundaryData, date: '3/31'},
        {name: '대구광역시', value: 'cherry-blossom-daegu', coords: daeguBoundaryData, date: '3/26'},
        {name: '울산광역시', value: 'cherry-blossom-ulsan', coords: ulsanBoundaryData, date: '3/27'},
        {name: '부산광역시', value: 'cherry-blossom-busan', coords: busanBoundaryData, date: '3/22'},
        {name: '여수시', value: 'cherry-blossom-yeosu', coords: yeosuBoundaryData, date: '3/28'},
        {name: '서귀포시', value: 'cherry-blossom-seogwipo', coords: seogwipoBoundaryData, date: '3/24'},
    ];

    const toggleTrashCanDropdown = () => {
        setIsTrashCanOpen(!isTrashCanOpen);
    };

    const toggleCherryBlossomDropdown = () => {
        setIsCherryBlossomOpen(!isCherryBlossomOpen);
    };

    const handleOutsideClick = (e) => {
        if (
            trashCanDropdownRef.current &&
            !trashCanDropdownRef.current.contains(e.target) &&
            cherryBlossomDropdownRef.current &&
            !cherryBlossomDropdownRef.current.contains(e.target)
        ) {
            setIsTrashCanOpen(false);
            setIsCherryBlossomOpen(false);
        }
    };

    const getLocation = async () => {
        return new Promise((resolve, reject) => {
            if (navigator.geolocation) {
                const now = new Date();
                navigator.geolocation.getCurrentPosition(
                    (position) => {
                        resolve({
                            err: 0,
                            time: now.toLocaleTimeString(),
                            latitude: position.coords.latitude,
                            longitude: position.coords.longitude,
                        });
                    },
                    () => {
                        resolve({
                            err: -1,
                            latitude: -1,
                            longitude: -1,
                        });
                    },
                    {enableHighAccuracy: true, maximumAge: 2000, timeout: 10000}
                );
            } else {
                reject({error: -2, latitude: -1, longitude: -1});
            }
        });
    };

    const fetchLocation = async () => {
        try {
            setIsLoading(true);
            const location = await getLocation();
            if (location.err === 0) {
                setCurrentPosition(new navermaps.LatLng(location.latitude, location.longitude));
            } else {
                setCurrentPosition(new navermaps.LatLng(37.3595704, 127.105399));
            }
            setIsLoading(false);
            // console.log(location.latitude);
            // console.log(location.longitude);
        } catch (error) {
            console.error('Error fetching location: ', error);
            setCurrentPosition(new navermaps.LatLng(37.3595704, 127.105399));
            setIsLoading(false);
        }
    };

    const getTrashCanLocation = async (e) => {
        const district = e.target.value;
        const checked = e.target.checked;

        if (checked) {
            const response = await fetch(
                `${TRASH_CAN_LOCATION}?value=${district}`,
                {
                    method: 'GET',
                }
            );

            if (response.status === 200) {
                const data = await response.json();
                const newMarkers = data.map((marker) => ({
                    ...marker,
                    infoWindow: new navermaps.InfoWindow({
                        content: `
              <div style="padding: 10px; background-color: #fff; border: 1px solid #ccc; border-radius: 5px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.3);">
                <h3 style="margin: 0 0 5px; font-size: 16px; font-weight: bold; color: #333;">${marker.district}</h3>
                <p style="margin: 0; font-size: 14px; color: #666;">
                  <span style="display: block; margin-bottom: 3px;">📍 ${marker.location}</span>
                  ${marker.district !== '관악구' ? `<span style="display: block; margin-bottom: 3px;">✨ ${marker.point}</span>` : ''}
                  <span>🗑️ ${marker.type}</span>
                </p>
              </div>
            `,
                    }),
                }));
                setMarkers((prevMarkers) => [...prevMarkers, ...newMarkers]);
                setCheckedLocations((prevLocations) => [...prevLocations, district]);
            }
        } else {
            setMarkers((prevMarkers) =>
                prevMarkers.filter((marker) => marker.district !== district)
            );
            setCheckedLocations((prevLocations) =>
                prevLocations.filter((location) => location !== district)
            );
        }
    };

    const locationListClickHandler = (e) => {
        if (e.target.tagName.toLowerCase() !== 'input') {
            const input = e.currentTarget.querySelector('input[type="checkbox"]');
            if (input) {
                input.checked = !input.checked;
                getTrashCanLocation({target: input});
            }
        }
    };

    const getCherryBlossomLocation = (e) => {
        const {value, checked} = e.target;
        setSelectedLocations((prevState) => ({
            ...prevState,
            [value]: checked,
        }));
    };

    const cherryBlossomListClickHandler = (e) => {
        if (e.target.tagName.toLowerCase() !== 'input') {
            const input = e.currentTarget.querySelector('input[type="checkbox"]');
            if (input) {
                input.checked = !input.checked;
                getCherryBlossomLocation({target: input});
            }
        }
    };

    const moveCameraToLocation = (coords) => {
        if (mapRef.current && coords.length > 0) {
            const bounds = new navermaps.LatLngBounds();
            coords.forEach((coord) => {
                bounds.extend(new navermaps.LatLng(coord[1], coord[0]));
            });
            mapRef.current.fitBounds(bounds);
        }
    };

    const closeInfoWindows = () => {
        infoWindows.forEach((infoWindow) => {
            infoWindow.close();
        });
        setInfoWindows([]);
    };

    const getPolygonCenter = (paths) => {
        const bounds = new navermaps.LatLngBounds();
        paths.forEach((path) => {
            bounds.extend(new navermaps.LatLng(path[1], path[0]));
        });
        return bounds.getCenter();
    };

    useEffect(() => {
        fetchLocation();
    }, []);

    useEffect(() => {
        document.addEventListener('mousedown', handleOutsideClick);

        return () => {
            document.removeEventListener('mousedown', handleOutsideClick);
        };
    }, []);

    // useEffect(() => {
    //     console.log(markers);
    // }, [markers]);
    //
    // useEffect(() => {
    //     console.log(checkedLocations);
    // }, [checkedLocations]);

    useEffect(() => {
        if (checkedLocations.length === 0) {
            setMarkers([]);
        }
    }, [checkedLocations]);

    useEffect(() => {
        const selectedCherryBlossomLocations = cherryblossomLocation.filter(
            (location) => selectedLocations[location.value]
        );

        if (selectedCherryBlossomLocations.length > 0) {
            const lastSelectedLocation = selectedCherryBlossomLocations[selectedCherryBlossomLocations.length - 1];
            moveCameraToLocation(lastSelectedLocation.coords);
        }
    }, [selectedLocations]);

    useEffect(() => {
        if (mapRef.current) {
            const listener = navermaps.Event.addListener(mapRef.current, 'click', () => {
                closeInfoWindows();
            });

            return () => {
                navermaps.Event.removeListener(listener);
            };
        }
    }, []);

    useEffect(() => {

        const token = localStorage.getItem("ACCESS_TOKEN");
        /**
         * 토큰을 가지고 있는 사용자의 토큰이 유효한지 확인하는 함수
         * @returns {Promise<void>}
         */
        const infomationValidate = async () => {
            const response = await fetch(TOKEN_VALIDATE_API_URL, {
                method: 'POST',
                headers: {
                    "Authorization": `Bearer ${localStorage.getItem("ACCESS_TOKEN")}`,
                }
            })

            if(response.status === 401) {
                localStorage.clear();
            }
        };

        if(token) {
            infomationValidate();
        }
    }, []);

    return (
        <div className={'map-area'}>
            <div className="btn-filter-group">
                <div className="trash-can-dropdown" ref={trashCanDropdownRef}>
                    <FaTrash className={'btn-trash-can'} onClick={toggleTrashCanDropdown}/>
                    <ul className={`trash-can-dropdown-menu ${isTrashCanOpen ? 'active' : ''}`}>
                        {trashCanLocation.map((location) => (
                            <li key={location.value} onClick={locationListClickHandler}>
                                {location.name}{' '}
                                <input
                                    type="checkbox"
                                    value={location.value}
                                    name={location.name}
                                    onChange={getTrashCanLocation}
                                />
                            </li>
                        ))}
                    </ul>
                </div>
                <div className="cherry-blossom-dropdown" ref={cherryBlossomDropdownRef}>
                    <IoMdFlower className={'btn-flower'} onClick={toggleCherryBlossomDropdown}/>
                    <ul className={`cherry-blossom-dropdown-menu ${isCherryBlossomOpen ? 'active' : ''}`}>
                        {cherryblossomLocation.map((location) => (
                            <li key={location.value} onClick={cherryBlossomListClickHandler}>
                                {location.name}{' '}
                                <input
                                    type="checkbox"
                                    value={location.value}
                                    name={location.name}
                                    onChange={getCherryBlossomLocation}
                                />
                            </li>
                        ))}
                    </ul>
                </div>
            </div>
            {isLoading ? (
                <Skeleton/>
            ) : (
                <NaverMap
                    defaultCenter={currentPosition || new navermaps.LatLng(37.5665, 126.9780)}
                    defaultZoom={10}
                    ref={mapRef}
                    onClick={closeInfoWindows}
                >
                    {currentPosition && <Marker position={currentPosition}/>}
                    {markers.map((marker, index) => (
                        <Marker
                            key={index}
                            position={new navermaps.LatLng(marker.latitude, marker.longitude)}
                            onClick={() => {
                                closeInfoWindows();
                                const markerInstance = new navermaps.Marker({
                                    position: new navermaps.LatLng(marker.latitude, marker.longitude),
                                });
                                marker.infoWindow.open(mapRef.current, markerInstance);
                                setInfoWindows([marker.infoWindow]);
                            }}
                        />
                    ))}
                    {cherryblossomLocation.map((location) =>
                            selectedLocations[location.value] && (
                                <React.Fragment key={location.value}>
                                    <Polygon
                                        paths={location.coords}
                                        fillColor={'#f1c4ff'}
                                        fillOpacity={0.3}
                                        strokeColor={'#c960ff'}
                                        strokeOpacity={0.6}
                                        strokeWeight={3}
                                    />
                                    <CustomOverlay
                                        content={location}
                                        map={mapRef.current}
                                        position={getPolygonCenter(location.coords)}
                                        navermaps={navermaps}
                                    />
                                </React.Fragment>
                            )
                    )}
                </NaverMap>
            )}
        </div>
    );
};

export default MapTemplate;