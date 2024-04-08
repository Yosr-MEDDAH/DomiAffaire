import { AbstractControl, ValidatorFn } from '@angular/forms';

export function strongPasswordValidator(firstName: string): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    const password = control.value;
    const hasFirstName = password
      .toLowerCase()
      .includes(firstName.toLowerCase());

    const containsLetter = /[a-zA-Z]/.test(password);
    const containsNumber = /\d/.test(password);
    const containsSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);

    const isLengthValid = password.length >= 8;

    const isValid =
      !hasFirstName &&
      containsLetter &&
      containsNumber &&
      containsSpecialChar &&
      isLengthValid;

    return isValid ? null : { invalidPassword: true };
  };
}
