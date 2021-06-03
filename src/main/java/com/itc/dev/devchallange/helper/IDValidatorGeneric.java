package com.itc.dev.devchallange.helper;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import com.itc.dev.devchallange.entity.Gender;
import com.itc.dev.devchallange.entity.IDNumberData;
import com.itc.dev.devchallange.entity.Nationality;
import com.itc.dev.devchallange.exception.IDNumberIsNotValidSAIdException;

public class IDValidatorGeneric {
	private LocalDate dateOfBirth;
	private int genderNum;
	private int citizenshipNum;
	private int checkBit;
	private Year pivotYear;

	private String idNumber;

	public boolean isValid(String idNumber) {
		IDValidatorGeneric validator = new IDValidatorGeneric();

		IDNumberData idNumberData = null;
		try {
			// if(idNumber.length() < 13)
			idNumberData = validator.parse(idNumber);
		} catch (IDNumberIsNotValidSAIdException ex) {
			// TODO Auto-generated catch block
			throw new IDNumberIsNotValidSAIdException(ex.getMessage());
		} catch (Exception ex) {
			/* // TODO Auto-generated catch block */
			throw new IDNumberIsNotValidSAIdException(ex.getMessage());
		}

		return idNumberData.isValid();
	}

	public static final DateTimeFormatter getTwoYearFormatter(Year pivotYear) {
		return new DateTimeFormatterBuilder().appendValueReduced(ChronoField.YEAR, 2, 2, pivotYear.getValue())
				.toFormatter();
	}

	private void breakDownIDNumber() {
		String birthDate = idNumber.substring(0, 6);

		if (pivotYear == null) {
			pivotYear = Year.of(Year.now().getValue() - 100);// Assume ID belongs to someone not older than 100 years
		}

		int year = Year.parse(birthDate.substring(0, 2), IDValidator.getTwoYearFormatter(pivotYear)).getValue();

		this.dateOfBirth = LocalDate.of(year, Month.of(Integer.parseInt(birthDate.substring(2, 4))),
				Integer.parseInt(birthDate.substring(4)));

		this.genderNum = Integer.parseInt(idNumber.substring(6, 10));
		this.citizenshipNum = Integer.parseInt(idNumber.substring(10, 11));
		this.checkBit = Integer.parseInt(idNumber.substring(12, 13));
	}

	public IDNumberData parse(String idNumber) throws Exception {
		if (idNumber.length() != 13) {
			throw new IDNumberIsNotValidSAIdException("ID Length invalid: ZA ID Number must be 13 digits long");
		}

		this.idNumber = idNumber;

		this.breakDownIDNumber();

		return new IDNumberData(idNumber, this.dateOfBirth, this.genderNum >= 5000 ? Gender.MALE : Gender.FEMALE,
				this.citizenshipNum == 0 ? Nationality.SOUTHAFRICAN
						: (this.citizenshipNum == 1 ? Nationality.NONSOUTHAFRICAN : Nationality.REFUGEE),
				this.checkBit == this.calculateCheckBit());
	}

	private int calculateCheckBit() {
		String withoutChecksum = idNumber.substring(0, idNumber.length() - 1);
		return Luhn.generate(withoutChecksum);
	}

	public IDValidatorGeneric setPivotYear(Year pivotYear) {
		this.pivotYear = pivotYear;
		return this;
	}
}
