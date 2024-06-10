import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CapitauxChartComponent } from './capitaux-chart.component';

describe('CapitauxChartComponent', () => {
  let component: CapitauxChartComponent;
  let fixture: ComponentFixture<CapitauxChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CapitauxChartComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CapitauxChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
