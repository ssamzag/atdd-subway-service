package nextstep.subway.line.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import nextstep.subway.common.domain.Name;
import nextstep.subway.line.domain.Color;

public final class LineCreateRequest {

    @NotBlank(message = "노선 이름은 비어있을 수 없습니다.")
    private String name;

    @NotBlank(message = "노선의 색상은 비어있을 수 없습니다.")
    private String color;

    @Valid
    @JsonUnwrapped
    private SectionRequest section;

    private LineCreateRequest() {
    }

    public LineCreateRequest(String name, String color, SectionRequest section) {
        this.name = name;
        this.color = color;
        this.section = section;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public SectionRequest getSection() {
        return section;
    }

    public Name name() {
        return Name.from(name);
    }

    public Color color() {
        return Color.from(color);
    }
}