import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArchivedClientsComponent } from './archived-clients.component';

describe('ArchivedClientsComponent', () => {
  let component: ArchivedClientsComponent;
  let fixture: ComponentFixture<ArchivedClientsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ArchivedClientsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ArchivedClientsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
