import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeadlineChartComponent } from './deadline-chart.component';

describe('DeadlineChartComponent', () => {
  let component: DeadlineChartComponent;
  let fixture: ComponentFixture<DeadlineChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DeadlineChartComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeadlineChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
