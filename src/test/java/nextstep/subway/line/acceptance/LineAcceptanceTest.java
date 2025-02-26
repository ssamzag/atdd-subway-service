package nextstep.subway.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능 인수 테스트")
public class LineAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 광교역;
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        lineRequest2 = new LineRequest("구신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 15);
    }

    /**
     * Given 지하철 역이 등록 되어 있고
     * When 지하철 노선을 등록하면
     * Then 지하철 노선이 등록된다
     *
     * When 지하철 노선을 조회하면
     * Then 지하철 노선이 조회된다
     *
     * When 지하철 노선 목록을 조회요청하면
     * Then 지하철 노선 목록이 조회된다
     *
     * When 기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성하면
     * Then 생성에 실패한다
     *
     * When 지하철 노선을 수정하면
     * Then 지하철 노선이 수정된다
     *
     * When 지하철 노선을 삭제하면
     * Then 지하철 노선이 삭제된다
     *
     * When 지하철 노선을 조회하면
     * Then 등록된 노선은 0개이다
     */
    @DisplayName("지하철 노선 관련 시나리오 테스트")
    @Test
    void scenarioTest() {
        // when
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(lineRequest1);
        // then
        지하철_노선_생성됨(createResponse);

        // when
        ExtractableResponse<Response> response2 = 지하철_노선_목록_조회_요청(createResponse);
        // then
        지하철_노선_응답됨(response2, createResponse);

        // when
        ExtractableResponse<Response> findResponse = 지하철_노선_목록_조회_요청();
        // then
        지하철_노선_목록_응답됨(findResponse);
        지하철_노선_목록_포함됨(findResponse, Collections.singletonList(createResponse));

        // when
        ExtractableResponse<Response> response3 = 지하철_노선_생성_요청(lineRequest1);
        // then
        지하철_노선_생성_실패됨(response3);

        // when
        ExtractableResponse<Response> response4 = 지하철_노선_수정_요청(createResponse, lineRequest2);
        // then
        지하철_노선_수정됨(response4);

        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선_제거_요청(createResponse);
        // then
        지하철_노선_삭제됨(removeResponse);

        // When
        ExtractableResponse<Response> findResponse2 = 지하철_노선_목록_조회_요청();

        // Then
        List<Long> ids = 지하철_노선_아이디_목록_가져옴(findResponse2);
        assertThat(ids).hasSize(0);

    }

    public static ExtractableResponse<Response> 지하철_노선_등록되어_있음(LineRequest params) {
        return 지하철_노선_생성_요청(params);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines")
                .then().log().all().
                        extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return 지하철_노선_목록_조회_요청("/lines");
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return 지하철_노선_목록_조회_요청(uri);
    }

    private static ExtractableResponse<Response> 지하철_노선_목록_조회_요청(String uri) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(LineResponse response) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", response.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(ExtractableResponse<Response> response, LineRequest params) {
        String uri = response.header("Location");

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured
                .given().log().all()
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철_노선_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_노선_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_응답됨(ExtractableResponse<Response> response, ExtractableResponse<Response> createdResponse) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(LineResponse.class)).isNotNull();
    }

    public static void 지하철_노선_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());

        List<Long> resultLineIds = 지하철_노선_아이디_목록_가져옴(response);

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    private static List<Long> 지하철_노선_아이디_목록_가져옴(ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());
    }

    public static void 지하철_노선_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
