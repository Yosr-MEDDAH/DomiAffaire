import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientsDocumentsComponent } from './clients-documents.component';

describe('ClientsDocumentsComponent', () => {
  let component: ClientsDocumentsComponent;
  let fixture: ComponentFixture<ClientsDocumentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClientsDocumentsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClientsDocumentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
