import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductoDetailComponent } from './product-detail.component';

describe('ProductDetailComponent', () => {
  let component: ProductoDetailComponent;
  let fixture: ComponentFixture<ProductoDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductoDetailComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ProductoDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
