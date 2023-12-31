// 마커를 담을 배열입니다
var markers = [];

var lat = document.getElementById("latclick1").value;
var lng = document.getElementById("lngclick1").value;

// 사용자가 목적지 탐색시 지정했던 장소의 위도와 경도의 존재 여부를 확인하고
// 그렇지 않다면 삼항 연산자를 사용해, 지도 중심의 기본값(현재 유성온천역)을 설정합니다.
// 이 값은 향후 사용자의 홈값 또는 GPS 값으로 대체될 수 있습니다.

var defaultLat = 36.353720; // 기본 위도 값
var defaultLng = 127.341445; // 기본 경도 값

lat = lat ? parseFloat(lat) : defaultLat;
lng = lng ? parseFloat(lng) : defaultLng;

var mapContainer = document.getElementById('map'), // 지도를 표시할 div
    mapOption = {
        center: new kakao.maps.LatLng(lat, lng),
        level: 4 // 지도의 확대 레벨
    };

// 지도를 생성합니다
var map = new kakao.maps.Map(mapContainer, mapOption);

// 장소 검색 객체를 생성합니다
var ps = new kakao.maps.services.Places();

// 검색 결과 목록이나 마커를 클릭했을 때 장소명을 표출할 인포윈도우를 생성합니다 (마커기준 z방향으로 1떨어진 위치)
var infowindow = new kakao.maps.InfoWindow({zIndex:1});
var simpleinfowindow = new kakao.maps.InfoWindow({zIndex:1});

document.getElementById("searchkeyword").addEventListener("click", toggleMenuWrap);

// 메뉴 스타일 토글 함수
function toggleMenuWrap() {
    var keywordValue = document.getElementById("keyword").value;
    var menuWrap = document.getElementById("menu_wrap");
    var menuWrap2 = document.getElementById("menu_wrap2");

    if (keywordValue.trim() == "") {
        // 키워드가 존재하지 않을 때
        menuWrap.style.display = "none";
        menuWrap2.style.display = "block";
    } else {
        // 키워드가 있을 때
        menuWrap.style.display = "block";
        menuWrap2.style.display = "none";
    }
}

// 키워드로 장소를 검색합니다
document.getElementById("searchkeyword").addEventListener("click", searchPlaces);

// 키워드 검색을 요청하는 함수입니다
function searchPlaces() {

    var keyword = document.getElementById('keyword').value;

    if (!keyword.replace(/^\s+|\s+$/g, '')) {
        alert('키워드를 입력해주세요!');
        return false;
    }

    // 장소검색 객체를 통해 키워드로 장소검색을 요청합니다
    ps.keywordSearch(keyword, placesSearchCB);
}

// 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
function placesSearchCB(data, status, pagination) {
    if (status === kakao.maps.services.Status.OK) {

        // 정상적으로 검색이 완료됐으면
        // 검색 목록과 마커를 표출합니다
        displayPlaces(data);

        // 페이지 번호를 표출합니다
        displayPagination(pagination);

    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {

        alert('검색 결과가 존재하지 않습니다.');
        return;

    } else if (status === kakao.maps.services.Status.ERROR) {

        alert('검색 결과 중 오류가 발생했습니다.');
        return;

    }

}

// 검색 결과 목록과 마커를 표출하는 함수입니다
function displayPlaces(places) {

    var listEl = document.getElementById('placesList'),
    menuEl = document.getElementById('menu_wrap'),
    fragment = document.createDocumentFragment(),
    bounds = new kakao.maps.LatLngBounds(),
    listStr = '';

    // 검색 결과 목록에 추가된 기존 항목들을 제거합니다
    removeAllChildNods(listEl);

    // 지도에 표시되고 있는 기존 마커를 제거합니다
    removeMarker();

    for ( var i=0; i < places.length; i++ ) {

        // 마커를 생성하고 지도에 표시합니다
        var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
            marker = addMarker(placePosition, i),
            itemEl = getListItem(i, places[i]); // 검색 결과 항목 Element를 생성합니다

        // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
        // LatLngBounds 객체에 좌표를 추가합니다
        bounds.extend(placePosition);

        // 마커와 검색결과 항목에 mouseover 했을때
        // 해당 장소에 인포윈도우에 장소명을 표시합니다
        // mouseout 했을 때는 인포윈도우를 닫습니다
        // 마커, 장소이름(기본예제의 title), 도로명주소, 지번주소명, 위도(세로선), 경도(가로선)
        (function(map, marker, pname, praddress, paddress, plat, plng) {

            // 맵을 클릭하면 발생하는 이벤트
            kakao.maps.event.addListener(map, 'click', function() {
                infowindow.close();
                simpleinfowindow.close();
            });

            // 마우스로 마커를 클릭하면 발생하는 이벤트
            kakao.maps.event.addListener(marker, 'click', function() {

                // 0. 간단 정보창 닫기
                simpleinfowindow.close();

                // 1. 정보창 표시
                displayInfowindow(marker, pname, praddress, paddress);

                // 2. 주소 정보들을 text 영역으로 전송 (hidden 사용)
                if (praddress) {
                document.getElementById('fulladdress').value = pname + "(" + praddress + ")";
                } else {
                document.getElementById('fulladdress').value = pname + "(" + paddress + ")";
                }

                document.getElementById('pname').value = pname;
                if (praddress) {
                    document.getElementById('paddress').value = praddress;
                } else {
                    document.getElementById('paddress').value = paddress;
                }
                document.getElementById('latclick').value = plat;
                document.getElementById('lngclick').value = plng;

                // 2. 길찾기 Get쿼리 추가 (탐험하기로 통합)

            })

            // 마우스를 마커 위에 두면 발생하는 이벤트
            kakao.maps.event.addListener(marker, 'mouseover', function() {
                displaysimpleInfowindow(marker, pname);
            });

            // 마우스를 마커 밖으로 옮기면 발생하는 이벤트
            kakao.maps.event.addListener(marker, 'mouseout', function() {
                simpleinfowindow.close();
            });

            // 리스트의 아이템을 클릭하면 발생하는 이벤트
            itemEl.onclick = function() {
                // 1. 주소 정보들을 text 영역으로 전송 (hidden 사용)

               // 2. 길찾기 Get쿼리 추가 (탐험하기로 통합)

            };

            // 아이템 리스트에 마우스를 올리면 발생하는 이벤트
            itemEl.onmouseover =  function () {
                displaysimpleInfowindow(marker, pname);
            };

            // 아이템 리스트에서 마우스를 제거하면 발생하는 이벤트
            itemEl.onmouseout =  function () {
                simpleinfowindow.close();
            };

        })(map, marker, places[i].place_name, places[i].road_address_name, places[i].address_name, places[i].y, places[i].x);

        fragment.appendChild(itemEl);

    }

    // 검색결과 항목들을 검색결과 목록 Elemnet에 추가합니다
    listEl.appendChild(fragment);
    menuEl.scrollTop = 0;

    // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
    map.setBounds(bounds);



}

// 검색결과 항목을 Element로 반환하는 함수입니다
function getListItem(index, places) {

    var el = document.createElement('li'),
    itemStr = '<span class="markerbg marker_' + (index+1) + '"></span>' +
                '<div class="info" style="cursor:pointer;">' +
                '   <h5>' + places.place_name + '</h5>';

    if (places.road_address_name) {
        itemStr += '    <span>' + places.road_address_name + '</span>' +
                    '   <span class="jibun gray">' +  places.address_name  + '</span>';
    } else {
        itemStr += '    <span>' +  places.address_name  + '</span>';
    }
      itemStr += '  <span class="tel">' + places.phone + '</span>'; +
                '</div>';

    el.innerHTML = itemStr;
    el.className = 'item';

    return el;
}


// 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
function addMarker(position, idx, title) {
    var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
        imageSize = new kakao.maps.Size(36, 37),  // 마커 이미지의 크기
        imgOptions =  {
            spriteSize : new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
            spriteOrigin : new kakao.maps.Point(0, (idx*46)+10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
            offset: new kakao.maps.Point(13, 37) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
        },
        markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
            marker = new kakao.maps.Marker({
            position: position, // 마커의 위치
            image: markerImage
        });

    marker.setMap(map); // 지도 위에 마커를 표출합니다
    markers.push(marker);  // 배열에 생성된 마커를 추가합니다

    return marker;
}

// 지도 위에 표시되고 있는 마커를 모두 제거합니다
function removeMarker() {
    for ( var i = 0; i < markers.length; i++ ) {
        markers[i].setMap(null);
    }
    markers = [];
}

// 검색결과 목록 하단에 페이지번호를 표시는 함수입니다
function displayPagination(pagination) {
    var paginationEl = document.getElementById('pagination'),
        fragment = document.createDocumentFragment(),
        i;

    // 기존에 추가된 페이지번호를 삭제합니다
    while (paginationEl.hasChildNodes()) {
        paginationEl.removeChild (paginationEl.lastChild);
    }

    for (i=1; i<=pagination.last; i++) {
        var el = document.createElement('a');
        el.href = "#";
        el.innerHTML = i;

        if (i===pagination.current) {
            el.className = 'on';
        } else {
            el.onclick = (function(i) {
                return function() {
                    pagination.gotoPage(i);
                }
            })(i);
        }

        fragment.appendChild(el);
    }
    paginationEl.appendChild(fragment);
}

function displaysimpleInfowindow(marker, title) {
    var content = '<div style="padding:5px;z-index:1;">' + title + '</div>';
    simpleinfowindow.setContent(content);
    simpleinfowindow.open(map, marker);
}

// 마커를 클릭했을 때 호출되는 함수입니다
function displayInfowindow(marker, pname, praddress, paddress) {

     var content = '<div class = "wrap">' +
                '		       <div class = "classimg"><img src = "/samplelogo.jpg" width="160" height="160"></div>' +
                         '        <div class="classinfo">' +
                            '            <div class="classtitle">' + pname + '</div>' +
                            '            <div class="classfounder">' + paddress + '</div>' +
                            '            <div class="classbutton">' +
                            '            <button type = "button" id = "selectstarting"> 출발지 </button>' +
                            '            <button type = "button" id = "selectdestination"> 도착지 </button>' +
                            '           </div>' +
                         '        </div>' +
                '			   </div>';

    infowindow.setContent(content);
    infowindow.open(map, marker);

    document.getElementById("selectstarting").addEventListener('click', getstarting);
    function getstarting(){
             document.getElementById('pstarting').value = document.getElementById('fulladdress').value;
             document.getElementById('pname1').value = document.getElementById('pname').value;
             document.getElementById('paddress1').value = document.getElementById('paddress').value;
             document.getElementById('latclick1').value = document.getElementById('latclick').value;
             document.getElementById('lngclick1').value = document.getElementById('lngclick').value;
    }

    document.getElementById("selectdestination").addEventListener('click', getdestination);
    function getdestination(){
             document.getElementById('pdestination').value = document.getElementById('fulladdress').value;
             document.getElementById('pname2').value = document.getElementById('pname').value;
             document.getElementById('paddress2').value = document.getElementById('paddress').value;
             document.getElementById('latclick2').value = document.getElementById('latclick').value;
             document.getElementById('lngclick2').value = document.getElementById('lngclick').value;
    }
}

 // 검색결과 목록의 자식 Element를 제거하는 함수입니다
function removeAllChildNods(el) {
    while (el.hasChildNodes()) {
        el.removeChild (el.lastChild);
    }
}








