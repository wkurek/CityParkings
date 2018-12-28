package util;

public class Validator {
    public static boolean isNameValid(String name) {
        return !name.isEmpty() && name.matches("[a-zA-Z]+$") && name.length() < 64;
    }

    public static boolean isSurnameValid(String surname) {
        return isNameValid(surname);
    }

    public static boolean isPhoneNumberValid(String phoneNumber) {
        return !phoneNumber.isEmpty() && phoneNumber.matches("[0-9]+$")
                && phoneNumber.length() <= 13 && phoneNumber.length() >= 9;
    }

    public static boolean isCityValid(String city) {
        return !city.isEmpty() && city.matches("[a-zA-Z ]+$") && city.length() < 64;
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
        return !plateNumber.isEmpty() && plateNumber.matches("[a-zA-Z0-9- ]+$") && plateNumber.length() < 9;
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

}
