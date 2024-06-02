import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateProfileAdminComponent } from './update-profile-admin.component';

describe('UpdateProfileAdminComponent', () => {
  let component: UpdateProfileAdminComponent;
  let fixture: ComponentFixture<UpdateProfileAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateProfileAdminComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateProfileAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
