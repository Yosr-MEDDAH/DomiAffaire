import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompanyCreationDocumentsComponent } from './company-creation-documents.component';

describe('CompanyCreationDocumentsComponent', () => {
  let component: CompanyCreationDocumentsComponent;
  let fixture: ComponentFixture<CompanyCreationDocumentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CompanyCreationDocumentsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CompanyCreationDocumentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
