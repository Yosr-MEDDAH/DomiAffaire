import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PacksChartComponent } from './packs-chart.component';

describe('PacksChartComponent', () => {
  let component: PacksChartComponent;
  let fixture: ComponentFixture<PacksChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PacksChartComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PacksChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
