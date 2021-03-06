package ua.com.kl.cmathtutor.service.strategy.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import ua.com.kl.cmathtutor.domain.entity.Ticket;
import ua.com.kl.cmathtutor.domain.entity.User;

@Slf4j
@Component
public class BirthdayDiscountStrategy extends AbstractDiscountStrategy {

    private int rangeDays = 5;

    @Override
    protected Collection<Double> getDiscountInPercentForSingleUserTickets(User owner, Collection<Ticket> tickets) {
	if (Objects.isNull(owner)) {
	    return Collections.nCopies(tickets.size(), 0d);
	}
	final Calendar nowCalendar = Calendar.getInstance();
	Calendar thisYearBirthdayCalendar = (Calendar) nowCalendar.clone();
	thisYearBirthdayCalendar.setTime(owner.getBirthdayDate());
	log.debug("Original birthday calendar is {}", thisYearBirthdayCalendar);
	thisYearBirthdayCalendar.set(Calendar.YEAR, nowCalendar.get(Calendar.YEAR));
	log.debug("This year birthday calendar is {}", thisYearBirthdayCalendar);
	Calendar nextYearBirthdayCalendar = (Calendar) thisYearBirthdayCalendar.clone();
	nextYearBirthdayCalendar.add(Calendar.YEAR, 1);
	log.debug("Next year birthday calendar is {}", nextYearBirthdayCalendar);
	Calendar previousYearBirthdayCalendar = (Calendar) thisYearBirthdayCalendar.clone();
	previousYearBirthdayCalendar.add(Calendar.YEAR, -1);
	log.debug("Previous year birthday calendar is {}", previousYearBirthdayCalendar);
	nowCalendar.set(Calendar.HOUR_OF_DAY, 0);
	nowCalendar.set(Calendar.MINUTE, 0);
	nowCalendar.set(Calendar.SECOND, 0);
	nowCalendar.set(Calendar.MILLISECOND, 0);
	Instant nowInstant = nowCalendar.toInstant();
	if (Math.abs(ChronoUnit.DAYS.between(nowInstant, thisYearBirthdayCalendar.toInstant())) < (rangeDays + 1)
		|| Math.abs(
			ChronoUnit.DAYS.between(nowInstant, previousYearBirthdayCalendar.toInstant())) < (rangeDays + 1)
		|| Math.abs(
			ChronoUnit.DAYS.between(nowInstant, nextYearBirthdayCalendar.toInstant())) < (rangeDays + 1)) {
	    return Collections.nCopies(tickets.size(), 5d);
	}
	return Collections.nCopies(tickets.size(), 0d);
    }

}
