import pymongo
from django.shortcuts import render
from .forms import RecipeForm

client = pymongo.MongoClient("localhost", 27017)
db = client.recipes
recipes = db.recipes


# Create your views here.
def get_recipe(request):
    if (request.method == 'POST'):
        form = RecipeForm(request.POST)
        amount_cal = form.amount_cal
        time = form.time
        recipe_name = form.recipe
        ingredients = form.ingredients
        if form.is_valid():
            recipes.insert(
                {'recipe': recipe_name, 'time': time, 'amount calories': amount_cal, "ingredients": ingredients})
            return render(request, 'recipes/recipes.html', {'recipes': recipes})
        else:
            form = RecipeForm()
            return render(request, 'recipes/index.html', {'form': form})


def get_recipes(request):
    if (request.method == 'GET'):
        return render(request, 'recipes/recipes.html', {'recipes': recipes})
