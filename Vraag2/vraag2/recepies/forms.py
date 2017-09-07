from django import forms


class RecipeForm(forms.Form):
    recipe = forms.CharField(label='recipe_name', max_length=100)
    amount_cal = forms.CharField('amount_cal', max_length=10)
    time = forms.DecimalField(min(0))
    ingredients = forms.TextInput()
