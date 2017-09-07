from django.conf.urls import url
from django.contrib import admin

from recepies import forms
from . import views

urlpatterns = [
    url(r'^admin/', admin.site.urls),
    url(r'*$', forms.RecipeName, name='index'),
    url(r'^(?P<recipes>[A-z]+)$', views.allrecipes)

]
