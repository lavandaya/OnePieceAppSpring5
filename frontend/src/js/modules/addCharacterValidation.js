import validator from "validator";

const { isURL, isInt, isFloat, isLength } = validator;

export function validateAddCharacterFields({ name, age, appearance, power }) {
    const errors = {};

    if (name !== null && !isLength(name, { min: 2, max: 50 })) {
        errors.name = "Name must be between 2 and 50 characters";
    }

    if (age !== null && !isInt(String(age), { min: 0, max: 200 })) {
        errors.age = "Age must be a whole number between 0 and 200";
    }

    if (
        appearance !== null
        && !isURL(appearance, {
            protocols: ["http", "https"],
            require_protocol: true,
            require_tld: false,
        })
    ) {
        errors.appearance = "Appearance must be a valid http(s) URL";
    }

    if (power !== null && !isFloat(String(power), { min: 0, max: 100 })) {
        errors.power = "Power must be a number between 0 and 100 DON";
    }

    return errors;
}
