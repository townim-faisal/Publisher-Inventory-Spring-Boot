
package rokomari.PublisherInventory.dummyData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import rokomari.PublisherInventory.model.admin.EndUser;
import rokomari.PublisherInventory.model.admin.UserRole;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.common.Address;
import rokomari.PublisherInventory.model.common.Area;
import rokomari.PublisherInventory.model.common.City;
import rokomari.PublisherInventory.model.common.Country;
import rokomari.PublisherInventory.model.user.general.Author;
import rokomari.PublisherInventory.model.user.purchase.Binder;
import rokomari.PublisherInventory.model.user.general.Book;
import rokomari.PublisherInventory.model.user.general.Category;
import rokomari.PublisherInventory.repository.admin.*;
import rokomari.PublisherInventory.repository.user.AuthorRepository;
import rokomari.PublisherInventory.repository.user.BinderRepository;
import rokomari.PublisherInventory.repository.user.BookRepository;
import rokomari.PublisherInventory.repository.user.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class DummyData implements ApplicationListener<ContextRefreshedEvent> {

    //private Logger log = Logger.getLogger(DummyData.class);


    private CityRepository cityRepository;
    private AreaRepository areaRepository;
    private CountryRepository countryRepository;
    private PublisherRepository publisherRepository;
    private UserRoleRepository endUserRoleRepository;
    private EndUserRepository endUserRepository;

    private AuthorRepository authorRepository;
    private BinderRepository binderRepository;
    private CategoryRepository categoryRepository;
    private BookRepository bookRepository;



    @Autowired
    public void setCityRepository(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }
    @Autowired
    public void setAreaRepository(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }
    @Autowired
    public void setCountryRepository(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }
    @Autowired
        public void setPublisherRepository(PublisherRepository publisherRepository) {
            this.publisherRepository = publisherRepository;
    }
    @Autowired
    public void setEndUserRoleRepository(UserRoleRepository endUserRoleRepository) {
        this.endUserRoleRepository = endUserRoleRepository;
    }
    @Autowired
    public void setEndUserRepository(EndUserRepository endUserRepository) {
        this.endUserRepository = endUserRepository;
    }
    @Autowired
    public void setAuthorRepository(AuthorRepository authorRepository) {
            this.authorRepository = authorRepository;
    }
    @Autowired
    public void setBinderRepository(BinderRepository binderRepository) {
        this.binderRepository = binderRepository;
    }
    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    @Autowired
    public void setCategoryRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        //Area, City, Country
        for (int i = 0;i<10;i++){
            City city = new City();
            city.setId(i);
            city.setName("City "+(i+1));
            cityRepository.save(city);

            Area area = new Area();
            area.setId(i);
            area.setName("Area "+(i+1));
            areaRepository.save(area);

            Country country = new Country();
            country.setId(i);
            country.setName("Country "+(i+1));
            countryRepository.save(country);
        }

        //Publisher
        for (int i = 0;i<3;i++){

            Publisher publisher = new Publisher();
            Address address = new Address();
            address.setId(i);
            /*address.setCity(cityRepository.findOne(i));
            address.setArea(areaRepository.findOne(i));
            address.setCountry(countryRepository.findOne(i));*/
            address.setAddressLine("Address Line "+(i+1));
            publisher.setId(i);
            publisher.setName("Publisher "+(i+1));
            publisher.setAddress(address);
            publisher.setPhone("10000"+i);
            publisher.setEmail("email"+i+"@com"+i+".com");
            publisherRepository.save(publisher);
        }

        //Role
        UserRole endUserRole = new UserRole();
        UserRole endUserRole1 = new UserRole();
        UserRole endUserRole2 = new UserRole();
        endUserRole.setRole("USER");
        endUserRoleRepository.save(endUserRole);
        endUserRole1.setRole("ADMIN");
        endUserRoleRepository.save(endUserRole1);
        endUserRole2.setRole("SUPER");
        endUserRoleRepository.save(endUserRole2);

        List<UserRole> endUserRoles1= new ArrayList<>();
        endUserRoles1.add(endUserRole);
        endUserRoles1.add(endUserRole1);

        List<UserRole> endUserRoles2= new ArrayList<>();
        endUserRoles2.add(endUserRole2);


        //User-1
        EndUser endUser1 = new EndUser();
        endUser1.setId(10);
        endUser1.setName("a");
        endUser1.setPassword("a");
        endUser1.setRoles(endUserRoles1);
        endUser1.setIsEnabled(true);
        endUser1.setPublisher(publisherRepository.findOneById(40));
        endUserRepository.save(endUser1);

        //User-2 - Admin
        EndUser endUser2 = new EndUser();
        endUser2.setId(20);
        endUser2.setName("b");
        endUser2.setPassword("b");
        endUser2.setRoles(endUserRoles2);
        endUser2.setIsEnabled(true);
        endUser2.setPublisher(publisherRepository.findOneById(41));
        endUserRepository.save(endUser2);

        //Author
        for (int i = 0;i<10;i++){

            Author author = new Author();
            author.setId(i);
            author.setName("Author "+i);
            author.setPhone("1000"+i);
            author.setPublisher(publisherRepository.findOneById(40));
            authorRepository.save(author);
        }

        //Binder
        for (int i = 0;i<10;i++){

            Binder binder = new Binder();
            binder.setId(i);
            binder.setName("Binder "+i);
            binder.setPhone("1000"+i);
            binder.setPublisher(publisherRepository.findOneById(40));
            binderRepository.save(binder);
        }

        //Category
        for (int i = 0;i<10;i++){

            Category category = new Category();
            category.setId(i);
            category.setName("Category "+i);
            category.setPublisher(publisherRepository.findOneById(40));
            categoryRepository.save(category);
        }

        List<Author> authors = new ArrayList<>();

        for (int i=0 ;i<5;i++){
            Author author = authorRepository.findOne(400+i);
            authors.add(author);
        }

        List<Category> categories = new ArrayList<>();

        for (int i=0 ;i<5;i++){
            Category category = categoryRepository.findOne(500+i);
            categories.add(category);
        }

        //Book
        for (int i = 0;i<23;i++){

            Book book = new Book();
            book.setId(i);
            book.setName("Book "+i);
            book.setAuthors(authors);
            book.setCategories(categories);
            book.setPublisher(publisherRepository.findOneById(40));
            bookRepository.save(book);
        }

    }
}
//