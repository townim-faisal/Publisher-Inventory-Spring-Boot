package rokomari.PublisherInventory.controller.user.purchase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.purchase.BinderOrder;
import rokomari.PublisherInventory.model.user.purchase.BinderOrderBook;
import rokomari.PublisherInventory.service.common.PublisherAndUser;
import rokomari.PublisherInventory.service.user.services.BinderOrderBookService;
import rokomari.PublisherInventory.service.user.services.BinderOrderService;
import rokomari.PublisherInventory.service.user.services.BinderService;
import rokomari.PublisherInventory.service.user.services.BookService;
import rokomari.PublisherInventory.service.common.HelpTo;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/binderOrder")
public class BinderOrderController {

    @Autowired
    BinderOrderService binderOrderService;

    @Autowired
    BinderOrderBookService binderOrderBookService;

    @Autowired
    BinderService binderService;

    @Autowired
    BookService bookService;

    @Autowired
    PublisherAndUser publisherAndUser;

    @Autowired
    HelpTo helpTo;

    private Publisher getCurrentPublisher(){

        return publisherAndUser.getCurrentPublisher();
    }


//    @PreBinderOrderize("hasBinderOrderity('ADMIN')")
    @RequestMapping(value="/new-binderOrder", method = RequestMethod.GET)
    public String newBinderOrder(Model model){

        publisherAndUser.publisherAndUser(model); // without this navbar will not appear

        BinderOrder binderOrder = new BinderOrder();

        binderOrder.setPublisher(getCurrentPublisher());
        model.addAttribute("page_title", "Place Order");
        model.addAttribute("addNewBinderOrder", binderOrder);
        model.addAttribute("allBinder", binderService.getAllBinders(getCurrentPublisher()));
        model.addAttribute("allBook", bookService.getAllBooks(getCurrentPublisher()));

        BinderOrderBook binderOrderBook = new BinderOrderBook();
        //binderOrderBook.setPublisher(getCurrentPublisher());
        model.addAttribute("addNewBinderOrderBooks", binderOrderBook);

        return "user/binderOrder/new";
    }

    // This method is serving both Create and Update operation

    @RequestMapping(value="/new-binderOrder", method = RequestMethod.POST)
    public String newBinderOrder(@Valid @ModelAttribute("addNewBinderOrder") BinderOrder binderOrder,
                                      BindingResult bindingResult,
                                      Model model, RedirectAttributes redirectAttributes){

        publisherAndUser.publisherAndUser(model);

        int currentId = binderOrder.getId(); // Storing the old value
        int serial = getSerial(binderOrder);

        List<BinderOrderBook> binderOrderBooks = binderOrder.getBinderOrderBooks();

        //Works while deleting items from Order
        for (Iterator<BinderOrderBook> binderOrderBook = binderOrderBooks.listIterator(); binderOrderBook.hasNext(); ) {

            BinderOrderBook binderOrderBook1 = binderOrderBook.next();

            if (binderOrderBook1.getBook()==null){
                binderOrderBook.remove();
            }
        }

        String orderId = getOrderId(binderOrder.getOrderPlaceDate(),serial);

        model.addAttribute("addNewBinderOrder", binderOrder);
        model.addAttribute("page_title", "Add BinderOrder");

        if (bindingResult.hasErrors()) {

            redirectAttributes.addFlashAttribute("status","unsuccessful");
            redirectAttributes.addFlashAttribute("message","Operation Unsuccessful");
            System.out.println("Error : "+bindingResult.getAllErrors());
            if(currentId == 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",orderId+" can not be added");

                return "redirect:/binderOrder/new-binderOrder";
            }
            else if(currentId > 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",orderId+" can not be updated");

                return "redirect:/binderOrder/edit/" + currentId;
            }
        }

        try{

            if(true || isOrderIdExist(binderOrder.getOrderId(),currentId).equals("true")){

                int totalQuantity = 0;
                double subtotal = 0;

                for (BinderOrderBook binderOrderBook1 : binderOrderBooks){
                    //binderOrderBook1.setPublisher(getCurrentPublisher());
                    binderOrderBook1.setBinderOrder(binderOrder);
                    totalQuantity += binderOrderBook1.getOrderQuantity();
                    subtotal += (binderOrderBook1.getOrderQuantity() * (binderOrderBook1.getUnitPrice()- binderOrderBook1.getUnitDiscount()));
                }

                binderOrder.setId(currentId);
                binderOrder.setOrderSerial(serial);
                binderOrder.setOrderId(orderId);
                binderOrder.setTotalQuantity(totalQuantity);
                binderOrder.setOrderSubTotalAmount(subtotal);
                binderOrder.setOrderTotalAmount(subtotal - binderOrder.getDiscount());
                binderOrder.setPayable(binderOrder.getOrderTotalAmount());
                binderOrder.setPaymentStatus(BinderOrder.PaymentStatus.NOT_PAID);
                binderOrder.setConfirmationStatus(BinderOrder.ConfirmationStatus.NOT_CONFIRMED);

                binderOrderService.createBinderOrder(binderOrder); // this operation changes the id number after creating new entry
                clearTextBoxes(binderOrder);
            }
            else{
                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",orderId+" can not be added or updated");

                return "redirect:/binderOrder/new-binderOrder";
            }

            binderOrder.setId(currentId); // setting the old value
            currentId= binderOrder.getId();

            if(currentId == 0){

                redirectAttributes.addFlashAttribute("status", "successful");
                redirectAttributes.addFlashAttribute("message",orderId+" added Successfully");
            }
            else if(currentId > 0){

                redirectAttributes.addFlashAttribute("status", "successful");
                redirectAttributes.addFlashAttribute("message",orderId+" updated Successfully");
            }
        }
        catch (Exception e){

            if(currentId == 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",orderId+" can not be added");
                return "redirect:/binderOrder/new-binderOrder";

            }
            else if(currentId > 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",orderId+" can not be updated");
                return "redirect:/binderOrder/edit/" + currentId;
            }
        }

        if(currentId == 0){

            return "redirect:/binderOrder/new-binderOrder";
        }

        else{

            return "redirect:/binderOrder/all-binderOrder";
        }
    }

    private int getSerial(BinderOrder binderOrder) {

        Calendar currentDate = binderOrder.getOrderPlaceDate();

        List<Integer> serials = new ArrayList<>();

        List<BinderOrder> binderOrders = binderOrderService.getAllOrdersByDate(getCurrentPublisher(),currentDate);

        if (binderOrders.size() == 0) {
            return 1;
        }
        else {
            if(binderOrder.getId() == 0){
                for (BinderOrder order : binderOrders) {
                    serials.add(order.getOrderSerial());
                }

                Arrays.sort(serials.toArray());

                return serials.get(serials.size() - 1) + 1;
            }
            else {

                if(binderOrderService.getBinderOrderByOrderPlaceDate(getCurrentPublisher(), binderOrder.getId(),currentDate)!= null){
                    return binderOrder.getOrderSerial();
                }
                else{
                    for (BinderOrder order : binderOrders) {
                        serials.add(order.getOrderSerial());
                    }

                    Arrays.sort(serials.toArray());

                    return serials.get(serials.size() - 1) + 1;
                }
            }
        }
    }

    private String getOrderId(Calendar calendar, int serial){

        String date = helpTo.convertDateForPoAndRo(calendar);

        String serialNo = String.valueOf(serial);

        if (serialNo.length()==1){
            serialNo = "00"+serialNo;
        }
        else if(serialNo.length()==2){
            serialNo = "0"+serialNo;
        }

        return String.valueOf("PO"+date+"-"+serialNo);
    }

    @RequestMapping(value = "/edit/{id}",method = RequestMethod.GET)
    public String editBinderOrder(@PathVariable int id, Model model ){

        publisherAndUser.publisherAndUser(model);

        BinderOrder binderOrder = binderOrderService.getBinderOrder(getCurrentPublisher(), id);

        publisherAndUser.publisherAndUser(model); // without this navbar will not appear
        model.addAttribute("page_title", "Update Order");
        model.addAttribute("addNewBinderOrder", binderOrder);
        model.addAttribute("allBinder", binderService.getAllBinders(getCurrentPublisher()));
        model.addAttribute("allBook", bookService.getAllBooks(getCurrentPublisher()));
        model.addAttribute("orderPlaceID", "Order No: "+ binderOrder.getOrderId());

        List<BinderOrderBook> binderOrderDetails = binderOrder.getBinderOrderBooks();
        model.addAttribute("addNewBinderOrderBooks", binderOrderDetails);

        return "user/binderOrder/new";
    }

    @RequestMapping(value = "/all-binderOrder", method = RequestMethod.GET)
    public String allBinderOrder(@RequestParam(value = "key", required = false) String searchKey, Model model, Pageable pageable){

        publisherAndUser.publisherAndUser(model);

        int elementPerPage = 5;
        int numberOfPage = 0;

        Page<BinderOrder> binderOrderList;

        pageable = new PageRequest(pageable.getPageNumber(),elementPerPage, Sort.Direction.ASC, "orderId");

        if(searchKey == null || searchKey.isEmpty() || searchKey.equals(" ")) {

            binderOrderList = binderOrderService.getAllBinderOrders(getCurrentPublisher(),pageable);
        }
        else {

            binderOrderList = binderOrderService.getAllBinderOrderWithSearchKey(getCurrentPublisher(),searchKey, pageable);
        }

        int totalElements = (int) binderOrderList.getTotalElements();
        if (totalElements % elementPerPage == 0) {
            numberOfPage = totalElements / elementPerPage;
        } else if (totalElements % elementPerPage > 0) {
            numberOfPage = (totalElements / elementPerPage) + 1;
        }

        model.addAttribute("page_title", "All Orders");
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("binderOrderList", binderOrderList);
        model.addAttribute("numberOfPage", numberOfPage);
        model.addAttribute("elementPerPage", elementPerPage);
        model.addAttribute("offset", pageable.getOffset());
        model.addAttribute("binderOrderNames",sortedBinderOrders(getCurrentPublisher()));
        model.addAttribute("searchKey", searchKey);
        model.addAttribute("previousPage", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("currentPage", pageable.getPageNumber());

        if(numberOfPage == pageable.next().getPageNumber()){
            model.addAttribute("nextPage", numberOfPage-1);
        }
        else {
            model.addAttribute("nextPage", pageable.next().getPageNumber());
        }

        return "user/binderOrder/all";
    }

    private List<BinderOrder> sortedBinderOrders(Publisher publisher){

        List<BinderOrder> binderOrdersSortedList = binderOrderService.getAllBinderOrders(publisher);
        Collections.sort(binderOrdersSortedList,(a, b) -> a.getOrderId().compareToIgnoreCase(b.getOrderId()));

        return binderOrdersSortedList;
    }

    @RequestMapping(value = "/search/", method = RequestMethod.GET)
    @ResponseBody
    public List<String> searchBinderOrder(@RequestParam("term") String searchKey){

        List<BinderOrder> binderOrdersSortedList = binderOrderService.getAllBinderOrderWithKey(getCurrentPublisher(), searchKey);
        Collections.sort(binderOrdersSortedList,(a, b) -> a.getOrderId().compareToIgnoreCase(b.getOrderId()));

        List<String> names = new ArrayList<>();

        for (BinderOrder binderOrder : binderOrdersSortedList){
            names.add(binderOrder.getOrderId());
        }

        return names;
    }

    @RequestMapping(value = "/binderOrder-detail/{id}", method = RequestMethod.GET)
    public String binderOrderDetail(@PathVariable int id, Model model){

        System.out.println("In order Detail");

        publisherAndUser.publisherAndUser(model);

        BinderOrder binderOrder = binderOrderService.getBinderOrder(getCurrentPublisher(),id);

        String OrderId = binderOrder.getOrderId();
        String binder = binderOrder.getBinder().getName();

        String orderDate = helpTo.convertDate(binderOrder.getOrderPlaceDate());

        int totalItemQuantities = binderOrder.getTotalQuantity();
        double orderSubTotal = binderOrder.getOrderSubTotalAmount();
        double discount = binderOrder.getDiscount();
        double orderTotalAmount = binderOrder.getOrderTotalAmount();

        List<BinderOrderBook> binderOrderBooks = binderOrderBookService.getBinderOrderBookList(getCurrentPublisher(), binderOrder);

        System.out.println("Order iTem detail size: "+ binderOrderBooks.size());

        for (BinderOrderBook binderOrderBook1 : binderOrderBooks){
            System.out.println("Item :"+ binderOrderBook1.getBook().getName());
        }


        model.addAttribute("page_title", "Order Detail");
        model.addAttribute("binder", binder);
        model.addAttribute("orderDate", orderDate);
        model.addAttribute("binderOrderId", id);
        model.addAttribute("OrderId", OrderId);
        model.addAttribute("binderOrderBooks", binderOrderBooks);
        model.addAttribute("totalItemQuantities", totalItemQuantities);
        model.addAttribute("orderSubTotal", orderSubTotal);
        model.addAttribute("discount", discount);
        model.addAttribute("orderTotalAmount", orderTotalAmount);
        model.addAttribute("binderOrder", binderOrder);

        return "user/binderOrder/orderDetails";
    }

    @RequestMapping(value="/setOrderStatus/{id}", method = RequestMethod.GET)
    public String setOrderStatus(@PathVariable int id, Model model ){

        System.out.println("ID in setOrder :"+id);
        publisherAndUser.publisherAndUser(model); // without this navbar will not appear
        BinderOrder binderOrder = binderOrderService.getBinderOrder(getCurrentPublisher(), id);

        binderOrder.setConfirmationStatus(BinderOrder.ConfirmationStatus.CONFIRMED);

        binderOrderService.createBinderOrder(binderOrder);

        String OrderId = binderOrder.getOrderId();
        String binder = binderOrder.getBinder().getName();

        String orderDate = helpTo.convertDate(binderOrder.getOrderPlaceDate());

        int totalItemQuantities = binderOrder.getTotalQuantity();
        double orderSubTotal = binderOrder.getOrderSubTotalAmount();
        double discount = binderOrder.getDiscount();
        double orderTotalAmount = binderOrder.getOrderTotalAmount();
        List<BinderOrderBook> binderOrderBooks = binderOrder.getBinderOrderBooks();

        model.addAttribute("page_title", "Order Detail");
        model.addAttribute("binderOrder", binderOrder);
        model.addAttribute("binder", binder);
        model.addAttribute("orderDate", orderDate);
        model.addAttribute("binderOrderId", id);
        model.addAttribute("OrderId", OrderId);
        model.addAttribute("binderOrderBooks", binderOrderBooks);
        model.addAttribute("totalItemQuantities", totalItemQuantities);
        model.addAttribute("orderSubTotal", orderSubTotal);
        model.addAttribute("discount", discount);
        model.addAttribute("orderTotalAmount", orderTotalAmount);
        model.addAttribute("confirmation", "Order Confirmed");

        return "user/binderOrder/orderDetails";
    }

    @RequestMapping(value = "/delete/{id}")
    public String deleteBinderOrder(@PathVariable int id, RedirectAttributes redirectAttributes){

        binderOrderService.delete(id);
        redirectAttributes.addFlashAttribute("status","successful");
        redirectAttributes.addFlashAttribute("message","Order Deleted");

        return "redirect:/binderOrder/all-binderOrder";
    }

    @RequestMapping("/isOrderIdExist")
    @ResponseBody
    public String isOrderIdExist(@RequestParam String orderId, @RequestParam int id){

        try{
            if(binderOrderService.getBinderOrderByOrderId(getCurrentPublisher(), orderId).getId() == id ||
                    binderOrderService.getBinderOrderByOrderId(getCurrentPublisher(), orderId) == null){

                return "true";
            }
            else {

                System.out.println("FALSE");
                return "false";
            }
        }
        catch (Exception e){

            return "true";
        }
    }


    private void clearTextBoxes(BinderOrder binderOrder){

        /*binderOrder.setPhone(null);
        binderOrder.setFacebook(null);
        binderOrder.setDateOfBirth(null);
        binderOrder.setName(null);
        binderOrder.setAddress(null);
        binderOrder.setEmail(null);*/
    }
}
