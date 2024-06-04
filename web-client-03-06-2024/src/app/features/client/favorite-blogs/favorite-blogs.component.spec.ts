import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FavoriteBlogsComponent } from './favorite-blogs.component';

describe('FavoriteBlogsComponent', () => {
  let component: FavoriteBlogsComponent;
  let fixture: ComponentFixture<FavoriteBlogsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FavoriteBlogsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FavoriteBlogsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
