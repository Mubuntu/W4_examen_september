from django.db import models


class Recipe(models.Model):
    name = models.CharField(max_length=50)
    calories = models.CharField(max_length=50)


    def __unicode__(self):
        return self.author_name


class Ingredient(models.Model):
    recipe_name= models.CharField(max_length=300)
    recipe_name = models.ForeignKey(Recipe)

    def __unicode__(self):
        return self.recipe_name