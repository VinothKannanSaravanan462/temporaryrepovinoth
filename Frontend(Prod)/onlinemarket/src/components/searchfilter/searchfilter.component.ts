import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProductService } from '../../services/product.service';
import { Observable } from 'rxjs';

interface ProductView {
  productid: number;
  name: string;
  description: string;
  avg_rating: number;
  subscription_count: number;
}

@Component({
  selector: 'app-searchfilter',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './searchfilter.component.html',
  styleUrl: './searchfilter.component.css'
})
export class SearchfilterComponent implements OnInit {

  searchForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    //private productDataService: ProductDataService
  ) { }

  ngOnInit(): void {
    this.searchForm = this.fb.group({
      productName: ['', [Validators.pattern(/^[a-zA-Z0-9\s]*$/)]], // Allow only alphanumeric characters and spaces
      count: ['', Validators.min(0)],
      rating: ['', [Validators.min(0), Validators.max(5)]]
    });
  }

  onSubmit() {
    console.log('onSubmit called'); // Debug log

    if (this.searchForm.valid) {
      const filters = this.searchForm.value;
      console.log('Form values:', filters); // Debug log

      let results$: Observable<ProductView[]> | undefined;

      if (filters.productName && !filters.rating && !filters.count) {
        results$ = this.productService.searchProductByName(filters.productName);
      } else if (!filters.productName && !filters.rating && filters.count) {
        results$ = this.productService.searchProductBySubsCount(filters.count);
      } else if (!filters.productName && filters.rating && !filters.count) {
        results$ = this.productService.searchProductByRating(filters.rating);
      } else if (!filters.productName && filters.rating && filters.count) {
        results$ = this.productService.searchProductBySubsCountAndRating(filters.count, filters.rating);
      } else if (filters.productName && !filters.rating && filters.count) {
        results$ = this.productService.searchProductByNameAndSubsCount(filters.productName, filters.count);
      } else if (filters.productName && filters.rating && !filters.count) {
        results$ = this.productService.searchProductByNameAndRating(filters.productName, filters.rating);
      } else if (filters.productName && filters.rating && filters.count) {
        results$ = this.productService.searchProductByNameSubsRating(filters.productName, filters.rating, filters.count);
      } else {
        results$ = this.productService.getProductList(); // When no filters are provided
      }

      if (results$) {
        console.log('Search observable created'); // Debug log
        results$?.subscribe(
          (results) => {
            console.log('Filtered Results Emitted:', results); // Debug log
            this.productService.setSearchResults(results);
          },
          (error) => {
            console.error('Error during search:', error);
            this.productService.setSearchResults([]); // Clear results on error
          }
        );
      }
    } else {
      console.log('Form is invalid.'); // Debug log
      this.productService.setSearchResults([]); // Clear any previous search results
      this.productService.signalInvalidSearch(); // Signal that the search was invalid
    }
  }

  get productNameControl() { return this.searchForm.get('productName'); }
  get countControl() { return this.searchForm.get('count'); }
  get ratingControl() { return this.searchForm.get('rating'); }

}