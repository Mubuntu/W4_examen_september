from django.db import models


class Recipe(models.Model):
    name = models.CharField(max_length=50)
    calories = models.CharField(max_length=50)
    ingredients = models.CharField(max_length=300)
    amount_cal = models.CharField(max_length=50)
    time = models.CharField(max_length=20)

    def __unicode__(self):
        return self.name
