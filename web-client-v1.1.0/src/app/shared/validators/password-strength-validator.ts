import { AbstractControl, ValidationErrors } from '@angular/forms';

export function passwordStrengthValidator(control: AbstractControl): ValidationErrors | null {
  const value: string = control.value;

  if (!value) return { required: 'Password is required.' };

  const errors: ValidationErrors = {};

  if (value.length < 8) {
    const remainingLength = 8 - value.length;
    errors['remainingLength'] = `You need ${remainingLength} more characters.`;
  }

  if (!/[a-z]/.test(value)) {
    errors['lowercase'] = 'Password must contain at least one lowercase letter.';
  }

  if (!/[A-Z]/.test(value)) {
    errors['uppercase'] = 'Password must contain at least one uppercase letter.';
  }

  if (!/[0-9]/.test(value)) {
    errors['number'] = 'Password must contain at least one number.';
  }

  if (!/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(value)) {
    errors['symbol'] = 'Password must contain at least one symbol.';
  }

  const hasErrors = Object.keys(errors).length !== 0;
  return hasErrors ? errors : null;
}
