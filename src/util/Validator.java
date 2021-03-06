package util;

import javafx.scene.control.TextField;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Validator {
    public static boolean isNameValid(String name) {
        return !name.isEmpty() && name.matches("[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+$") && name.length() < 64;
    }

    public static boolean isSurnameValid(String surname) {
        return isNameValid(surname);
    }

    public static boolean isPhoneNumberValid(String phoneNumber) {
        return !phoneNumber.isEmpty() && phoneNumber.matches("[0-9 +]+$")
                && phoneNumber.length() <= 15 && phoneNumber.length() >= 9;
    }

    public static boolean isLatitudeValid(String lat)
    {
        float result;
        try {
            result = Float.parseFloat(lat);
        }
        catch (NumberFormatException e)
        {
            return false;
        }
        return result<=90.0f && result>=-90.0f;
    }
    public static boolean isLongitudeValid(String lon)
    {
        float result;
        try {
            result = Float.parseFloat(lon);
        }
        catch (NumberFormatException e)
        {
            return false;
        }
        return result<=180.0f && result>=-180.0f;
    }
    public static boolean isCityValid(String city) {
        return !city.isEmpty() && city.matches("[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+$") && city.length() < 64;
    }

    public static boolean isStreetValid(String street) {
        return isCityValid(street);
    }

    public static boolean isZIPCodeValid(String zipCode) {
        return !zipCode.isEmpty() && zipCode.matches("^(\\d){2}-(\\d){3}$");
    }

    public static boolean isNumberValid(String number) {
        return !number.isEmpty()
                && number.matches("^(\\d){1,8}$");
    }

    public static boolean isPlateNumberValid(String plateNumber) {
        return !plateNumber.isEmpty() && plateNumber.matches("[a-zA-Z0-9- ]+$") && plateNumber.length() <=9;
    }

    public static boolean isWeightValid(String weight) {
        try{
            Float.parseFloat(weight);
        } catch(NumberFormatException e){
            return false;
        }

        return true;
    }

    public static boolean isHeightValid(String height) {
        return isWeightValid(height);
    }

    public static boolean isIntegerInputValid(String integer)
    {
        try{
            Integer.parseInt(integer);
        }
        catch (NumberFormatException e)
        {
            return false;
        }
        return true;
    }


    public static boolean isSalaryValid(String salary) {
        try{
            Float.parseFloat(salary);
        } catch(NumberFormatException e){
            return false;
        }

        return true;
    }

    public static boolean isDateValid(String date) {
        String DATE_FORMAT = "yyy-MM-dd";
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
