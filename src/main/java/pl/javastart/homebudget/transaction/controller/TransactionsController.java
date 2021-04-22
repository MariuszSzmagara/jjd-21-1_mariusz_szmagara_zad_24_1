package pl.javastart.homebudget.transaction.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.javastart.homebudget.transaction.dao.TransactionsDao;
import pl.javastart.homebudget.transaction.model.Transaction;
import pl.javastart.homebudget.transaction.model.Type;

import java.util.List;
import java.util.Optional;

@Controller
public class TransactionsController {

    private final TransactionsDao transactionsDao;

    public TransactionsController(TransactionsDao transactionsDao) {
        this.transactionsDao = transactionsDao;
    }

    @GetMapping("/newTransaction")
    public String createNewTransaction(Model model) {
        model.addAttribute("newTransaction", new Transaction());
        return "newTransactionForm";
    }

    @PostMapping("/addNewTransaction")
    public String addNewTransaction(Transaction transaction) {
        transactionsDao.add(transaction);
        return "successAdded";
    }

    @GetMapping("/chooseTransactionToModifyOrDelete")
    public String chooseTransactionToModify(Model model) {
        List<Transaction> allTransactionsList = transactionsDao.getAll();
        model.addAttribute("allTransactionsList", allTransactionsList);
        return "chooseTransactionToModifyOrDelete";
    }

    @GetMapping("/modifyTransactionById")
    public String editTransactionById(@RequestParam int id, Model model) {
        Optional<Transaction> requestById = transactionsDao.getById(id);
        if (requestById.isPresent()) {
            model.addAttribute("transaction", requestById.get());
            return "modifyTransactionForm";
        } else {
            return "error";
        }
    }

    @PostMapping("/updateTransactionById")
    public String updateTransactionById(Transaction transaction) {
        transactionsDao.updateById(transaction);
        return "successModifyOrDelete";
    }

    @GetMapping("/deleteTransactionById")
    public String deleteTransactionById(int id) {
        transactionsDao.deleteById(id);
        return "successModifyOrDelete";
    }

    @GetMapping("/chooseTransactionType")
    public String chooseTransactionType(Type transactionType, Model model) {
        model.addAttribute("transactionType", transactionType);
        return "chooseTransactionType";
    }

    @GetMapping("/transactionsListByType")
    public String getTransactionsListByType(@RequestParam Type type, Model model) {
        List<Transaction> transactionsListByType = transactionsDao.searchByType(type);
        model.addAttribute("transactionsListByType", transactionsListByType);
        model.addAttribute("type", type);
        return "transactionsListByType";
    }
}
