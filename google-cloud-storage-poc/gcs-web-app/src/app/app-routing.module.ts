import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {DropFileComponent} from './drop-file/drop-file.component';
import {FilesPreviewComponent} from './files-preview/files-preview.component';

const routes: Routes = [{
  path: 'filePreview', component: FilesPreviewComponent
},
{
  path: 'home', component: DropFileComponent
}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
