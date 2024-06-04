import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DomiciliarionHistoryComponent } from './domiciliarion-history.component';

describe('DomiciliarionHistoryComponent', () => {
  let component: DomiciliarionHistoryComponent;
  let fixture: ComponentFixture<DomiciliarionHistoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DomiciliarionHistoryComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DomiciliarionHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
