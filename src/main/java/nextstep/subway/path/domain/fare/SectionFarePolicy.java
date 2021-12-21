package nextstep.subway.path.domain.fare;

import java.util.List;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class SectionFarePolicy implements FarePolicy<Fare> {
    private Fare fare = Fare.ZERO;

    public SectionFarePolicy(List<Section> sections, List<Station> stations) {
        for (int i = 0; i < stations.size() - 1; i++) {
            Station station1 = stations.get(i);
            Station station2 = stations.get(i + 1);

            updateMaxIfExists(sections, station1, station2);
            updateMaxIfExists(sections, station2, station1);
        }
    }

    private void updateMaxIfExists(List<Section> sections, Station up, Station down) {
        sections.stream()
            .filter(section -> section.hasUpStation(up) && section.hasDownStation(down))
            .findFirst()
            .ifPresent(this::updateMaxFare);
    }

    private void updateMaxFare(Section section) {
        Fare fare = section.getFare();

        if (this.fare.compareTo(fare) < 0) {
            this.fare = fare;
        }
    }

    @Override
    public Fare calculateFare(Fare fare) {
        return this.fare.add(fare);
    }
}