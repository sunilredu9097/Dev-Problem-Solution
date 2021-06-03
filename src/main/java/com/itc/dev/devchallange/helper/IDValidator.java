package com.itc.dev.devchallange.helper;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.itc.dev.devchallange.entity.Gender;
import com.itc.dev.devchallange.entity.IDNumberData;
import com.itc.dev.devchallange.entity.Nationality;

public class IDValidator implements ConstraintValidator<ValidIdNumber, String> {
	private LocalDate dateOfBirth;
	private int genderNum;
	private int citizenshipNum;
	private int checkBit;
	private Year pivotYear;

	private String idNumber;

	@Override
	public boolean isValid(String idNumber, ConstraintValidatorContext constraintValidatorContext) {
		IDValidator validator = new IDValidator();

		IDNumberData idNumberData = null;
		try {
			idNumberData = validator.parse(idNumber);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			throw new Exception("ID Length invalid: ZA ID Number must be 13 digits long");
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

	public IDValidator setPivotYear(Year pivotYear) {
		this.pivotYear = pivotYear;

		return this;
	}

	/*
	 * public static void main(String[] args) throws Exception { IDValidator
	 * idNumberParser = new IDValidator(); IDNumberData idNumberData =
	 * idNumberParser.parse("4001014800084");
	 * 
	 * System.out.println("isValid: " + idNumberData.isValid());
	 * System.out.println("Date Of Birth: " +
	 * idNumberData.getBirthdate().format(DateTimeFormatter.ofLocalizedDate(
	 * FormatStyle.LONG))); System.out.println("Age: " + idNumberData.getAge());
	 * System.out.println("Gender: " + idNumberData.getGender());
	 * System.out.println("Citizenship: " + idNumberData.getCitizenShip()); }
	 */
}
