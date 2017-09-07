from django.shortcuts import render
import pymongo
client = pymongo.MongoClient("localhost", 27017)
db = client.recipes
recipes = db.recipes

# Create your views here.
def index(request):

    return render(request, 'recipes/index.html', {'recipes': recipes})
