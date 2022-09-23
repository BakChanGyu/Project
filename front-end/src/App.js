import React, {useState, useEffect} from "react";
import axios from 'axios';
import './App.css';

function App() {
    // 요청받을 변수를 담아줄 변수 선언, 여러개의 변수 받기
    const [data, setData] = useState({
        // 변수 초기화
        id: 0,
        loginId: '',
        password: '',
        name: '',
    });


    // data 상태값을 디스트럭쳐링
    const {id, loginId, password, name} = data;

    useEffect(() => {
        axios.post('/api/add', {
            id: '96',
            loginId: 'Jung',
            password: '1234',
            name: 'Lee'
        }).then(response => {
            console.log(response.data);
            setData(response.data);
        }).catch(error => {
            alert(error);
        });
        // 컴포넌트가 화면에 가장 처음 렌더링될때 한 번만 실행하고 싶을때는 빈 배열
    }, []);

    return (
        <div>
            <h2> {id} </h2>
            <h2> {loginId} </h2>
            <h2> {password} </h2>
            <h2> {name} </h2>
            <p> 회원가입 완료!</p>
        </div>
    );
}

export default App;

/**
 * 문제
 * 1. 버전이 안맞는지 읽어오기가 안됨 -> pakage.json버전 맞춰보기
 * 2. api Post쓰려면 로그인, 회원가입때 보내주는 파라미터명 맞춰야 함.
 */

/*
import React from 'react';
import MainPage from './pages/MainPage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom';

function App() {

  return (
    <BrowserRouter>
      <div className="App">
        <Routes>
          <Route path="/" exact element={<MainPage />} />
          <Route path="/login" exact element={<LoginPage />} />
          <Route path="/register" exact element={<RegisterPage />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;
*/