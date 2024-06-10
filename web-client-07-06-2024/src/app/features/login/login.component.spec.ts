import { TestBed, ComponentFixture, async } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './login.component';
import { By } from '@angular/platform-browser';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [ReactiveFormsModule],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize signinForm correctly', () => {
    expect(component.signinForm).toBeDefined();
    expect(component.signinForm.controls['email']).toBeDefined();
    expect(component.signinForm.controls['pwd']).toBeDefined();
  });

  it('should validate email field', () => {
    let email = component.signinForm.controls['email'];
    expect(email.valid).toBeFalsy();

    email.setValue('test@test.com');
    expect(email.valid).toBeTruthy();

    email.setValue('invalidemail');
    expect(email.valid).toBeFalsy();
  });

  it('should call onSubmit method on form submission', () => {
    spyOn(component, 'onSubmit');
    let form = fixture.debugElement.query(By.css('form'));
    form.triggerEventHandler('ngSubmit', null);
    expect(component.onSubmit).toHaveBeenCalled();
  });
});
