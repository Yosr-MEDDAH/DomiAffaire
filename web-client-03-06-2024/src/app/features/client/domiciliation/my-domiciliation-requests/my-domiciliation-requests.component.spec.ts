import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyDomiciliationRequestsComponent } from './my-domiciliation-requests.component';

describe('MyDomiciliationRequestsComponent', () => {
  let component: MyDomiciliationRequestsComponent;
  let fixture: ComponentFixture<MyDomiciliationRequestsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MyDomiciliationRequestsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MyDomiciliationRequestsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
