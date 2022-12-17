import React, { useEffect, useState } from "react";
import styled from "styled-components";
// import axios from "../API/axios";
import Geocode from "react-geocode";
import axios from "axios";
import CurrWeather from "../Components/CurrWeather";
import RecommendClothes from "../Components/RecommendClothes";
import OptionButton from "../Components/OptionButton";
import { dummy } from "../OptionDummy";
// MainPage에서
// 시간정보, 주소 정보를 back에 요청할 수 있도록 데이터를 가공....

Geocode.setApiKey("AIzaSyAoKq3Uq6CfDSQ91bccZ17H4-DGo-SnTQw");
Geocode.setLanguage("en");
Geocode.setRegion("en");

export default function MainPage({ location }) {
  // const [weather, setWeather] = useState({});
  const [city, setCity] = useState("");
  const [result, setResult] = useState({});
  const [user, setUser] = useState(false);
  const [gender, setGender] = useState(-1);
  const [sensitivity, setSensitivity] = useState(-1);

  const API_KEY = "011be7fcc3f5c002bed4737f3e97b02a";
  const url = `https://api.openweathermap.org/data/2.5/weather?q=${city}&appid=${API_KEY}`;

  useEffect(() => {
    console.log("나는 ", city);
    getWeather();
  }, [city]);

  useEffect(() => {
    console.log("user : ", user);
    // sessionStorage.getItem("user");
  }, [user]);

  useEffect(() => {
    console.log("gender : ", gender);
  }, [gender]);

  useEffect(() => {
    console.log("sensitivity", sensitivity);
  }, [sensitivity]);

  const getWeather = () => {
    Geocode.fromLatLng(location.coordinates.lat, location.coordinates.lng).then(
      async (response) => {
        const address = response.results[0].formatted_address.split(",");
        setCity(address[2]);
        // console.log("address: ", address[2]);
        // console.log(typeof address[2]);
        // console.log("city: ", city);

        if (city !== "") {
          const data = await axios({
            method: "get",
            url: url,
          });
          setResult(data);
          console.log(data);
        }
      },
      (error) => {
        console.error("error", error);
      }
    );
  };

  // 현재 시간 정보 받기

  return (
    <div>
      {Object.keys(result).length !== 0 && (
        <div>
          {/* 현재 날씨 정보 props: current-weather-info */}
          <CurrWeather
            address={city}
            weather={result.data.weather[0].main}
            temp={result.data.main.temp}
          ></CurrWeather>

          {/* 남자 여자 선택하는 버튼 만들기, props={ gender, setGender } */}
          {/* 온도 민감도 선택하는 버튼 만들기 props={ sensitivity, setsensitivity }*/}
          <RootWrap>
            {dummy.map((item) => (
              <OptionButton
                key={item.idx}
                title={item.title}
                OptionList={item.OptionList}
                setOption={item.title === "성별" ? setGender : setSensitivity}
              />
            ))}
          </RootWrap>

          {/* 옷 추천 props: temperature */}
          <RecommendClothes temp={result.data.main.temp}></RecommendClothes>

          {/* <Clothes temp={}></Clothes> */}
          {/* 리뷰 : pros: location */}
          {/* <Review location={city}></Review> */}

          {/* <Review location={location}></Review> */}
          {/* 시간별 날씨 정보 hourly-weather-info  */}
          {/* <Row weatherInfo={}></Row> */}
        </div>
      )}
    </div>
  );
}

const RootWrap = styled.div`
  // position: absolute;
  // top: 0;
  // bottom: 0;
  // width: 100%;
  // max-width: 500px;
  // left: 50%;
  // transform: translate(-50%, 0);

  // background-color: white;

  // padding: 20px;
`;
