import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HandleBlogComponent } from './handle-blog.component';

describe('HandleBlogComponent', () => {
  let component: HandleBlogComponent;
  let fixture: ComponentFixture<HandleBlogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HandleBlogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HandleBlogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
