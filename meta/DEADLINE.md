

## Part 1.1: App Description

> My app allows users to select and compare the exchange rates between
> two global currencies using the names and codes of the currencies provided
> by the exchange-api. The rates are then provided by the CurrencyBeacon API.
> This app then allows users to visualize their geographic relation from
> a world map with markers provided by the Geoapify API.


TODO WRITE / REPLACE

## APIs

### API 1

```
https://raw.githubusercontent.com/fawazahmed0/exchange-api/main/country.json
```

> Used to get names of countries and their respective currency codes.

### API 2

```
https://api.currencybeacon.com/v1/convert?api_key=mVIzTknXa8E9jSC094StUnXG8J9STgTT&from=AOA&to=XOF&amount=1
```

> Used to get currency exchange rates.
> 5000 API requests allowed on free plan.

### API 3

```
https://api.geoapify.com/v1/geocode/search?text=benin&format=json&apiKey=d66530321e5c4294be3496040ea1f0be
https://maps.geoapify.com/v1/staticmap?style=osm-carto&width=800&height=600&center=lonlat:4.5,0&zoom=0.809&marker=lonlat:17.5691241,-11.8775768;color:%23ff0000;size:medium|lonlat:2.2584408,9.5293472;color:%23ff0000;size:medium&apiKey=d66530321e5c4294be3496040ea1f0be
```
> Used to get geolocation and static map that displays the world map with markers indicating the two countries selected by the user.
> 3000 API requests per month allowed on free plan.

## Part 2: New

> Something new that I learned from this project was
> that not all APIs may be fit to use for a desired
> purpose, and that it is totally fine to have to
> make major changes if something doesn't work out.
> Although, it was frustrating at times, this was a
> definitely a very important learning experience
> for my growth as a programmer. Now that I have gained
> valuable experience in building applications, I am
> excited to continue doing so.



## Part 3: Retrospect

> If you could start the project over from scratch, what do
> you think might do differently and why?
> If I could start the project over from scratch, I think
> I would do a better job at handling errors, testing, and
> overall user-friendliness. Before working on this project,
> I don't think I have ever encountered so many errors at a
> time before, but now that I am more familiar with working
> with these problems, the process would probably be a lot
> more efficient if I was to start over. Additionally, I
> would probably focus more on making the app more user-
> friendly, also.


