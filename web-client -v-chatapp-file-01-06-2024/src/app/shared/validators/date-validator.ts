import { AbstractControl, ValidationErrors } from '@angular/forms';

function calculateAge(birthDate: string): number {
  const today = new Date();
  const birth = new Date(birthDate);
  let age = today.getFullYear() - birth.getFullYear();
  const monthDiff = today.getMonth() - birth.getMonth();
  if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
    age--;
  }
  return age;
}

export function invalidDateValidator(control: AbstractControl): ValidationErrors | null {
  const birthDate = control.value;
  if (!birthDate) {
    return null;
  }

  const age = calculateAge(birthDate);

  if (age < 18 || age > 120) {
    return { invalidDate: true };
  }

  return null;
}
