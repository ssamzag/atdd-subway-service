package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.function.Predicate;

public enum AgeDiscountType {
    ADULT(0, 0, age -> age == null || age >= 19),
    CHILD(50, 350, age -> age >= 6 && age < 13),
    TEENAGER(20, 350, age -> age >= 13 && age < 19),
    FREE(100, 0, age -> age < 6);

    private final int discountRate;
    private final int discountFare;
    private final Predicate<Integer> ageMatchingExp;

    AgeDiscountType(int discountRate, int discountFare, Predicate<Integer> ageMatchingExp) {
        this.discountRate = discountRate;
        this.discountFare = discountFare;
        this.ageMatchingExp = ageMatchingExp;
    }

    public static AgeDiscountType of(Integer age) {
        return Arrays.stream(values())
                .filter(s -> s.ageMatchingExp.test(age))
                .findFirst()
                .orElse(ADULT);
    }

    public int calculate(int fare) {
        return (int) ((fare - discountFare) * (1 - (discountRate * 0.01)));
    }
}
