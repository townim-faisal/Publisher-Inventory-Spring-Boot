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
import rokomari.PublisherInventory.model.user.inventory.InventoryDetails;
import rokomari.PublisherInventory.model.user.purchase.*;
import rokomari.PublisherInventory.service.common.HelpTo;
import rokomari.PublisherInventory.service.common.PublisherAndUser;
import rokomari.PublisherInventory.service.user.services.*;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/binderReceive")
public class BinderReceiveController {

    @Autowired
    BinderReceiveService binderReceiveService;

    @Autowired
    BinderOrderBookService binderOrderBookService;

    @Autowired
    BinderService binderService;

    @Autowired
    BookService bookService;

    @Autowired
    InventoryDetailsService inventoryDetailsService;

    @Autowired
    PublisherAndUser publisherAndUser;

    @Autowired
    HelpTo helpTo;

    @Autowired
    BinderOrderService binderOrderService;

    private Publisher getCurrentPublisher(){

        return publisherAndUser.getCurrentPublisher();
    }


//    @PreBinderReceiveOrderize("hasBinderReceiveOrderity('ADMIN')")
    @RequestMapping(value="/new-binderReceive", method = RequestMethod.GET)
    public String newBinderReceiveOrder(Model model){

        publisherAndUser.publisherAndUser(model); // without this navbar will not appear

        BinderReceive binderReceive = new BinderReceive();

        binderReceive.setPublisher(getCurrentPublisher());

        model.addAttribute("page_title", "Select Orders");
        model.addAttribute("addNewBinderReceive", binderReceive);
        model.addAttribute("allBinder", binderService.getAllBinders(getCurrentPublisher()));

        return "user/binderReceive/orders";
    }

    @RequestMapping(value="/orders", method = RequestMethod.POST)
    public String setOrders(@Valid @ModelAttribute("addNewBinderReceive") BinderReceive binderReceive,
                            BindingResult bindingResult,
                            Model model, RedirectAttributes redirectAttributes){

        publisherAndUser.publisherAndUser(model); // without this navbar will not appear

        List<BinderOrder> binderOrders = binderReceive.getBinderOrders();
        List<BinderOrder> binderOrders1 = new ArrayList<>();

        for (BinderOrder binderOrder : binderOrders){

            binderOrder = binderOrderService.getBinderOrder(getCurrentPublisher(), binderOrder.getId());
            binderOrders1.add(binderOrder);
        }

        binderOrders = binderOrders1;

        binderReceive.setBinderOrders(binderOrders);

        binderReceive.setPublisher(getCurrentPublisher());
        model.addAttribute("page_title", "Receive Order(s)");
        model.addAttribute("addNewBinderReceive", binderReceive);
        /*model.addAttribute("orders", binderOrders);*/

        model.addAttribute("receiveDate", helpTo.convertDate(binderReceive.getOrderReceiveDate()));

        /*for (BinderOrder binderOrder : binderOrders){
            System.out.println("============ ID : "+binderOrder.getId());
            System.out.println("============ Order Serial : "+binderOrder.getOrderSerial());
            System.out.println("============ Order ID : "+binderOrder.getOrderId());
        }*/

        return "user/binderReceive/new";
    }

    // This method is serving both Create and Update operation
    @RequestMapping(value="/new-binderReceive", method = RequestMethod.POST)
    public String newBinderReceiveOrder(@Valid @ModelAttribute("addNewBinderReceive") BinderReceive binderReceive,
                                      BindingResult bindingResult,
                                      Model model, RedirectAttributes redirectAttributes){

        publisherAndUser.publisherAndUser(model);

        System.out.println("Receiving date : "+ binderReceive.getOrderReceiveDate());

        List<BinderOrder> binderOrders = binderReceive.getBinderOrders();

        int currentId = binderReceive.getId(); // Storing the old value
        int serial=0;

        if (currentId==0){
            serial = getSerial(binderReceive);
        }
        else if (currentId>0){
            serial = binderReceive.getReceiveSerial();
        }

        //List<BinderOrderBook> binderOrderItemDetails = binderReceive.getBinderOrderBooks();


        //Works while deleting items from Order
        /*for (int i = 0; i < binderOrderItemDetails.size(); i++) {

            BinderOrderBook itemDetails = binderOrderItemDetails.get(i);

            if (itemDetails.getBook()==null){

                binderOrderItemDetails.remove(i);
                i--;
            }
        }*/

        String orderId = getOrderId(binderReceive,serial);

        model.addAttribute("addNewBinderReceive", binderReceive);
        model.addAttribute("page_title", "Add BinderReceive");

        if (bindingResult.hasErrors()) {

            redirectAttributes.addFlashAttribute("status","unsuccessful");
            redirectAttributes.addFlashAttribute("message","Operation Unsuccessful");
            System.out.println("Error : "+bindingResult.getAllErrors());
            if(currentId == 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",orderId+" can not be added");

                return "redirect:/binderReceive/new-binderReceive";
            }
            else if(currentId > 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",orderId+" can not be updated");

                return "redirect:/binderReceive/edit/" + currentId;
            }
        }

        try{

            if(true || isOrderIdExist(binderReceive.getReceiveId(),currentId).equals("true")){

                int totalQuantity = 0;
                double subtotal = 0;

                List<BinderReceiveBook> binderReceiveBookList = new ArrayList<>();

                for(BinderOrder binderOrder : binderOrders) {
                    for (BinderOrderBook binderOrderBook : binderOrder.getBinderOrderBooks()){

                        BinderReceiveBook binderReceiveBook = new BinderReceiveBook();
                        //binderOrderBook.setPublisher(getCurrentPublisher());

                        BinderOrderBook binderOrderBook1 = binderOrderBookService.getBinderOrderBook(binderOrder, binderOrderBook.getId());

                        //binderOrderBook1.setDeliveredQuantity(binderOrderBook1.getDeliveredQuantity() + binderOrderBook.getDeliveredQuantity());

                        binderReceiveBook.setBinderOrderBook(binderOrderBook1);
                        binderReceiveBook.setBinderReceive(binderReceive);
                        binderReceiveBook.setBinderOrder(binderOrder);
                        //binderReceiveBook.setPublisher(getCurrentPublisher());
                        binderReceiveBook.setReceiveQuantity(binderOrderBook.getReceivedQuantity());
                        binderReceiveBook.setBook(binderOrderBook1.getBook());
                        binderReceiveBook.setUnitPrice(binderOrderBook1.getUnitPrice());
                        binderReceiveBook.setUnitDiscount(binderOrderBook1.getUnitDiscount());
                        binderReceiveBook.setOrderQuantity(binderOrderBook1.getOrderQuantity());

                        System.out.println("ordered qty :"+ binderOrderBook1.getOrderQuantity());

                        System.out.println("Receive qty: "+ binderOrderBook.getReceivedQuantity());

                        //binderOrderBookService.createBinderOrderBook(binderOrderBook1);

                        totalQuantity += binderOrderBook.getReceivedQuantity();
                        subtotal += (binderOrderBook.getReceivedQuantity() * (binderOrderBook1.getUnitPrice() - binderOrderBook1.getUnitDiscount()));
                        System.out.println("Remaining :"+ binderOrderBook.getRemainingQuantity());

                        binderReceiveBookList.add(binderReceiveBook);
                    }

                }

                binderReceive.setBinderReceiveBooks(binderReceiveBookList);
                binderReceive.setId(currentId);
                binderReceive.setReceiveSerial(serial);
                binderReceive.setReceiveId(orderId);
                binderReceive.setReceiveTotalQuantity(totalQuantity);
                binderReceive.setReceiveTotalAmount(subtotal);
                binderReceive.setConfirmationStatus(BinderReceive.ConfirmationStatus.NOT_CONFIRMED);

                /*binderReceive.set(subtotal);
                binderReceive.setOrderTotalAmount(subtotal - binderReceive.getDiscount());
                binderReceive.setPayable(binderReceive.getOrderTotalAmount());
                binderReceive.setPaymentStatus(BinderReceive.PaymentStatus.NOT_PAID);
                binderReceive.setConfirmationStatus(BinderReceive.ConfirmationStatus.NOT_CONFIRMED);
*/
                /*System.out.println("Came to before save");*/
                binderReceiveService.createBinderReceive(binderReceive); // this operation changes the id number after creating new entry
  //              clearTextBoxes(binderReceive);
            }
            else{
                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",orderId+" can not be added or updated");

                return "redirect:/binderReceive/new-binderReceive";
            }

            binderReceive.setId(currentId); // setting the old value
            currentId= binderReceive.getId();

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

            System.out.println("ERROR!!!"+e.getMessage());
            //System.out.println("ERROR!!!"+e.printStackTrace(new PrintStream()));

            if(currentId == 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",orderId+" can not be added");
                return "redirect:/binderReceive/new-binderReceive";

            }
            else if(currentId > 0){

                redirectAttributes.addFlashAttribute("status", "unsuccessful");
                redirectAttributes.addFlashAttribute("message",orderId+" can not be updated");
                return "redirect:/binderReceive/edit/" + currentId;
            }
        }

        if(currentId == 0){

            return "redirect:/binderReceive/new-binderReceive";
        }

        else{

            return "redirect:/binderReceive/all-binderReceive";
        }
    }
    private int getSerial(BinderReceive binderReceive) {

        Calendar currentDate = binderReceive.getOrderReceiveDate();

        List<Integer> serials = new ArrayList<>();

        List<BinderReceive> binderReceives = binderReceiveService.getAllOrdersByDate(getCurrentPublisher(),currentDate);

        if(binderReceives.size()==0){
            return 1;
        }
        else{
            for(BinderReceive order : binderReceives){
                serials.add(order.getReceiveSerial());
            }

            Arrays.sort(serials.toArray());

            return serials.get(serials.size()-1)+1;
        }
    }

    private String getOrderId(BinderReceive binderReceive, int serial){

        Calendar calendar = binderReceive.getOrderReceiveDate();

        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String serialNo = String.valueOf(serial);

        if (month.length()<2){
            month = "0"+month;
        }
        if (day.length()<2){
            day = "0"+day;
        }

        if (serialNo.length()==1){
            serialNo = "00"+serialNo;
        }
        else if(serialNo.length()==2){
            serialNo = "0"+serialNo;
        }

        return String.valueOf("RO-"+year+"-"+month+"-"+day+"-"+serialNo);
    }

    @RequestMapping(value = "/edit/{id}",method = RequestMethod.GET)
    public String editBinderReceiveOrder(@PathVariable int id, Model model ){

        publisherAndUser.publisherAndUser(model);

        BinderReceive binderReceive = binderReceiveService.getBinderReceive(getCurrentPublisher(), id);

        publisherAndUser.publisherAndUser(model); // without this navbar will not appear
        model.addAttribute("page_title", "Update Order");
        model.addAttribute("addNewBinderReceive", binderReceive);
        model.addAttribute("allBinder", binderService.getAllBinders(getCurrentPublisher()));
        model.addAttribute("allBook", bookService.getAllBooks(getCurrentPublisher()));

        List<BinderReceiveBook> binderReceiveBooks = binderReceive.getBinderReceiveBooks();
        model.addAttribute("addNewBinderReceiveBooks", binderReceiveBooks);

        return "user/binderReceive/new";
    }

    @RequestMapping(value = "/all-binderReceive", method = RequestMethod.GET)
    public String allBinderReceiveOrder(@RequestParam(value = "key", required = false) String searchKey, Model model, Pageable pageable){

        publisherAndUser.publisherAndUser(model);

        int elementPerPage = 5;
        int numberOfPage = 0;

        Page<BinderReceive> binderReceiveList;

        pageable = new PageRequest(pageable.getPageNumber(),elementPerPage, Sort.Direction.ASC, "receiveId");

        if(searchKey == null || searchKey.isEmpty() || searchKey.equals(" ")) {

            binderReceiveList = binderReceiveService.getAllBinderReceives(getCurrentPublisher(),pageable);
        }
        else {

            binderReceiveList = binderReceiveService.getAllBinderReceivesWithSearchKey(getCurrentPublisher(),searchKey, pageable);
        }

        int totalElements = (int) binderReceiveList.getTotalElements();
        if (totalElements % elementPerPage == 0) {
            numberOfPage = totalElements / elementPerPage;
        } else if (totalElements % elementPerPage > 0) {
            numberOfPage = (totalElements / elementPerPage) + 1;
        }

        model.addAttribute("page_title", "All Orders");
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("binderReceiveList", binderReceiveList);
        model.addAttribute("numberOfPage", numberOfPage);
        model.addAttribute("elementPerPage", elementPerPage);
        model.addAttribute("offset", pageable.getOffset());
        model.addAttribute("binderReceiveNames",sortedBinderReceiveOrders(getCurrentPublisher()));
        model.addAttribute("searchKey", searchKey);
        model.addAttribute("previousPage", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("currentPage", pageable.getPageNumber());

        if(numberOfPage == pageable.next().getPageNumber()){
            model.addAttribute("nextPage", numberOfPage-1);
        }
        else {
            model.addAttribute("nextPage", pageable.next().getPageNumber());
        }

        return "user/binderReceive/all";
    }

    private List<BinderReceive> sortedBinderReceiveOrders(Publisher publisher){

        List<BinderReceive> binderReceivesSortedList = binderReceiveService.getAllBinderReceives(publisher);
        Collections.sort(binderReceivesSortedList,(a, b) -> a.getReceiveId().compareToIgnoreCase(b.getReceiveId()));

        return binderReceivesSortedList;
    }

    @RequestMapping(value = "/search/", method = RequestMethod.GET)
    @ResponseBody
    public List<String> searchBinderReceiveOrder(@RequestParam("term") String searchKey){

        List<BinderReceive> binderReceivesSortedList = binderReceiveService.getAllBinderReceivesWithKey(getCurrentPublisher(), searchKey);
        Collections.sort(binderReceivesSortedList,(a, b) -> a.getReceiveId().compareToIgnoreCase(b.getReceiveId()));

        List<String> names = new ArrayList<>();

        for (BinderReceive binderReceive : binderReceivesSortedList){
            names.add(binderReceive.getReceiveId());
        }

        return names;
    }
    @RequestMapping(value = "/binderReceive-detail/{id}", method = RequestMethod.GET)
    public String binderReceiveDetail(@PathVariable int id, Model model){

        publisherAndUser.publisherAndUser(model);

        BinderReceive binderReceive = binderReceiveService.getBinderReceive(getCurrentPublisher(),id);

        String receiveId = binderReceive.getReceiveId();
        String binder = binderReceive.getBinder().getName();

        String orderDate = helpTo.convertDate(binderReceive.getOrderReceiveDate());

        int totalItemQuantities = binderReceive.getReceiveTotalQuantity();
        //double orderSubTotal = binderReceive.getOrderSubTotalAmount();
        //double discount = binderReceive.getDiscount();
        double orderTotalAmount = binderReceive.getReceiveTotalAmount();

        List<BinderOrder> binderOrders = binderReceive.getBinderOrders();
        List<BinderReceiveBook> binderReceiveDetails = binderReceive.getBinderReceiveBooks();

        for (BinderReceiveBook binderReceiveBook1 : binderReceiveDetails){
//            for (BinderOrderBook binderOrderDetails : binderOrder.getBinderOrderBooks()){
                System.out.println("Order ID: "+ binderReceiveBook1.getId());
                System.out.println("Received Qty: "+ binderReceiveBook1.getReceiveQuantity());
//            }
        }


        model.addAttribute("page_title", "Receive Detail");
        model.addAttribute("binder", binder);
        model.addAttribute("orderDate", orderDate);
        model.addAttribute("binderReceiveId", id);
        model.addAttribute("receiveId", receiveId);
        model.addAttribute("binderReceive", binderReceive);
        model.addAttribute("totalItemQuantities", totalItemQuantities);
//        model.addAttribute("orderSubTotal", orderSubTotal);
//        model.addAttribute("discount", discount);
        model.addAttribute("orderTotalAmount", orderTotalAmount);
        model.addAttribute("binderReceive", binderReceive);

        return "user/binderReceive/receiveDetails";
    }

    @RequestMapping(value="/setOrderStatus/{id}", method = RequestMethod.GET)
    public String setOrderStatus(@PathVariable int id, Model model ){

        System.out.println("ID in setOrder :"+id);
        publisherAndUser.publisherAndUser(model); // without this navbar will not appear
        BinderReceive binderReceive = binderReceiveService.getBinderReceive(getCurrentPublisher(), id);

        binderReceive.setConfirmationStatus(BinderReceive.ConfirmationStatus.CONFIRMED);

        for (BinderReceiveBook binderReceiveBook : binderReceive.getBinderReceiveBooks()){

            BinderOrderBook binderOrderBook = binderOrderBookService.getBinderOrderBook(getCurrentPublisher(),binderReceiveBook.getBinderOrderBook().getId());
            binderOrderBook.setReceivedQuantity(binderReceiveBook.getReceiveQuantity());

            try {

                InventoryDetails inventoryDetails = new InventoryDetails();

                if (inventoryDetailsService.getInventoryDetails(binderReceiveBook.getBook()) == null){
                    inventoryDetails.setBook(binderReceiveBook.getBook());
                    inventoryDetails.setBookQuantity(binderReceiveBook.getReceiveQuantity());
                    /*inventoryDetails.setPublisher(getCurrentPublisher());*/
                }
                else{

                    inventoryDetails = inventoryDetailsService.getInventoryDetails(binderReceiveBook.getBook());
                    inventoryDetails.setBookQuantity(inventoryDetails.getBookQuantity()+binderReceiveBook.getReceiveQuantity());
                }

                inventoryDetailsService.createInventoryDetails(inventoryDetails);
            }
            catch (Exception e){
                System.out.println("Exception during inventory update :"+e.getMessage());
            }

            //InventoryDetails inventoryDetails = new InventoryDetails();

            binderOrderBookService.createBinderOrderBook(binderOrderBook);
        }

        binderReceiveService.createBinderReceive(binderReceive);

        String receiveId = binderReceive.getReceiveId();
        String binder = binderReceive.getBinder().getName();

        String orderDate = helpTo.convertDate(binderReceive.getOrderReceiveDate());

        int totalItemQuantities = binderReceive.getReceiveTotalQuantity();
        //double orderSubTotal = binderReceive.;
        //double discount = binderReceive.g;
        double orderTotalAmount = binderReceive.getReceiveTotalAmount();
        List<BinderReceiveBook> binderReceiveBooks = binderReceive.getBinderReceiveBooks();

        model.addAttribute("page_title", "Receive Detail");
        model.addAttribute("binder", binder);
        model.addAttribute("binderReceive", binderReceive);
        model.addAttribute("orderDate", orderDate);
        model.addAttribute("binderReceiveId", id);
        model.addAttribute("receiveId", receiveId);
        model.addAttribute("items", binderReceiveBooks);
        model.addAttribute("totalItemQuantities", totalItemQuantities);
        //model.addAttribute("orderSubTotal", orderSubTotal);
       // model.addAttribute("discount", discount);
        model.addAttribute("orderTotalAmount", orderTotalAmount);
        model.addAttribute("confirmation", "Order Confirmed");

        return "user/binderReceive/receiveDetails";
    }
    @RequestMapping(value = "/delete/{id}")
    public String deleteBinderReceiveOrder(@PathVariable int id, RedirectAttributes redirectAttributes){

        binderReceiveService.delete(id);
        redirectAttributes.addFlashAttribute("status","successful");
        redirectAttributes.addFlashAttribute("message","Order Deleted");

        return "redirect:/binderReceive/all-binderReceive";
    }
    @RequestMapping("/isOrderIdExist")
    @ResponseBody
    public String isOrderIdExist(@RequestParam String orderId, @RequestParam int id){

        /*try{
            if(binderReceiveService.getBinderReceiveOrderByOrderId(getCurrentPublisher(), orderId).getId() == id ||
                    binderReceiveService.getBinderReceiveOrderByOrderId(getCurrentPublisher(), orderId) == null){

                return "true";
            }
            else {

                System.out.println("FALSE");
                return "false";
            }
        }
        catch (Exception e){

            return "true";
        }*/

        return "true";
    }

    /*private void clearTextBoxes(BinderReceive binderReceive){

        binderReceive.setPhone(null);
        binderReceive.setFacebook(null);
        binderReceive.setDateOfBirth(null);
        binderReceive.setName(null);
        binderReceive.setAddress(null);
        binderReceive.setEmail(null);
    }*/

    @RequestMapping(value = "/ordersByBinder/{id}", method = RequestMethod.GET)
    @ResponseBody
    public List<BinderOrder> ordersByBinder(@PathVariable int id){

        Binder binder = binderService.getBinder(getCurrentPublisher(),id);

        System.out.println("Binder : "+binder.getName());

        List<BinderOrder> allConfirmedOrder = binderOrderService.getAllConfirmedOrder(getCurrentPublisher(),binder, BinderOrder.ConfirmationStatus.CONFIRMED);

        for (BinderOrder order : allConfirmedOrder){
            System.out.println("Orders : "+order.getOrderId());
        }

        return allConfirmedOrder;
    }

    @RequestMapping(value = "/allBinders", method = RequestMethod.GET)
    @ResponseBody
    public List<Binder> binders(){

        List<Binder> binders = binderService.getAllBinders(getCurrentPublisher());
        return binders;
    }

}
