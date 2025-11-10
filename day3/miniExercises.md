## Mini exercise 1 DOM manipulation

- Open [this HTML file](code/domManipulation/exercise.html)
- Follow the instructions in there

## Mini exercise 2 Light switch

- Create an app where you can turn a lamp on and off
- Light bulb: display a lamp emoji (💡 when on, when off/grey lamp emoji) or use images
- Switch: Create a clickable switch button
- When lamp is OFF have grey lamp, light background
- When lamp is ON have a yellow lamp with glow, dark background
- OR: do it the other way around, dark room until the light is on.
- Optional: When hovering over the switch, make it bigger (scale)
- Optional: Show "Lamp is ON" or "Lamp is OFF" below the switch

## Mini exercise 3 XMLHttpRequest recipes

- Create a recipe app that fetches data from the JSONPlaceholder API
- API endpoint to use: `https://dummyjson.com/recipes`
- Use `XMLHttpRequest` to make the API call
- Display on the page:
  - A list of recipe names
  - For each recipe, show: name, image, cooking time, and difficulty level
  - Number of recipes loaded
- Implement **loading state**: Show "Loading recipes..." while the data is being fetched
- Implement **error handling**: If the request fails, display "Failed to load recipes. Please try again."
- Style the recipes nicely with CSS (cards, grid layout, etc.)
- Optional: Add a "Refresh" button to reload the recipes
- Optional: Show the ingredients list when clicking on a recipe card

## Mini exercise 4 Fetch recipes

- Create the same recipe app as in exercise 3, but now use the modern `fetch()` API instead of XMLHttpRequest
- API endpoint to use: `https://dummyjson.com/recipes`
- Use `fetch()` with `.then()` and `.catch()` OR `async/await` with try/catch
- Display the same information as in exercise 3
- Implement **loading state**: Show "Loading recipes..." while the data is being fetched
- Implement **error handling**: If the request fails, display "Failed to load recipes. Please try again."
- Compare: Notice how much cleaner the code is compared to XMLHttpRequest!
- Optional: Add a search input to filter recipes by name
- Optional: Add buttons to filter by difficulty level (Easy, Medium, Hard)