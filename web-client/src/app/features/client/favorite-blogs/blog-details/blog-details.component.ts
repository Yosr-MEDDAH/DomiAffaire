import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ClientService } from 'src/app/core/services/client.service';

@Component({
  selector: 'app-blog-details',
  templateUrl: './blog-details.component.html',
  styleUrls: ['./blog-details.component.css']
})
export class BlogDetailsComponent implements OnInit {
  blog: any;

  constructor(
    private route: ActivatedRoute,
    private clientService: ClientService
  ) {}

  ngOnInit(): void {
    // Get the blog id from the route parameters
    this.route.paramMap.subscribe((params) => {
      const blogId = params.get('id');
      if (blogId) {
        // Call the service method to fetch the blog details
        this.clientService.getBlogByID(blogId).subscribe((data: any) => {
          this.blog = data;
          console.log('Fetched Blog Details:', this.blog);
        });
      }
    });
  }
  

 


 
}