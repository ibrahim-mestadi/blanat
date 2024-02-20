public class Main {
    
    public record Product(String name, BigDecimal price) {}
    public record City(String name, List<Product> products) {}

    public static void solver() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, City> cities = new HashMap<>();
        for (String line : lines) {
            String[] parts = line.split(",");
            String cityName = parts[0];
            String productName = parts[1];
            BigDecimal price = new BigDecimal(parts[2]);
            if (cities.containsKey(cityName)) {
                City city = cities.get(cityName);
                List<Product> products = city.products();
                products.add(new Product(productName, price));
                cities.put(cityName, new City(cityName, products));
            } else {
                List<Product> products = new ArrayList<>();
                products.add(new Product(productName, price));
                cities.put(cityName, new City(cityName, products));
            }
        }
        Map<String, BigDecimal> cityPrices = new HashMap<>();
        for (Map.Entry<String, City> entry : cities.entrySet()) {
            String cityName = entry.getKey();
            City city = entry.getValue();
            List<Product> products = city.products();
            BigDecimal totalPrice = products.stream().map(Product::price).reduce(BigDecimal.ZERO, BigDecimal::add);
            cityPrices.put(cityName, totalPrice);
        }
        Map.Entry<String, BigDecimal> cheapestCity = cityPrices.entrySet().stream().min(Map.Entry.comparingByValue()).get();
        City cheapestCityData = cities.get(cheapestCity.getKey());
        List<Product> cheapestProducts = cheapestCityData.products().stream().sorted(Comparator.comparing(Product::price).thenComparing(Product::name)).limit(5).collect(Collectors.toList());
        StringBuilder result = new StringBuilder();
        result.append(cheapestCity.getKey()).append(" ").append(cheapestCity.getValue()).append("\n");
        for (Product product : cheapestProducts) {
            result.append(product.name()).append(" ").append(product.price()).append("\n");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
            writer.write(result.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        solver();
    }
}