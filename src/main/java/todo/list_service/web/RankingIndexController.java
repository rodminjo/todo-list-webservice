package todo.list_service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import todo.list_service.config.auth.LoginUser;
import todo.list_service.config.auth.dto.SessionUser;
import todo.list_service.service.ranking.RankingService;

import java.time.LocalDate;
import java.time.temporal.WeekFields;

@RequiredArgsConstructor
@Controller
public class RankingIndexController {
    private final RankingService rankingService;

    @GetMapping("/ranking")
    public String todayInputIndex(){
        LocalDate date = LocalDate.now();
        LocalDate endDate = date.plusDays(7 - date.get(WeekFields.ISO.dayOfWeek()));
        int weekOfYear = date.get(WeekFields.ISO.weekOfYear()) <= 52 ? date.get(WeekFields.ISO.weekOfYear()) : 1;
        String week = endDate.getYear() +"-"+ weekOfYear;

        return "redirect:/ranking/" + week;
    }

    @GetMapping("/ranking/{week}")
    public String rankingIndex(@PathVariable String week,
                               @RequestParam(value = "search", required = false) String search,
                               Model model, @LoginUser SessionUser user){
        model.addAttribute("week", week);

        if (user!= null) {
            model.addAttribute("userName", user.getName());

        }

        if (search != null){
            System.out.println("search = " + search);
            model.addAttribute("rankingList", rankingService.findByWeekAndNickName(week, search));
        }else {
            model.addAttribute("rankingList", rankingService.findByWeek(week));

        }

        return "ranking-index";
    }
    @GetMapping("/ranking/{week}/{m}")
    public String rankingIndexMove(@PathVariable String week,
                                   @PathVariable Integer m){
        String[] split = week.split("-");
        Integer currentWeek = Integer.valueOf(split[1]);
        if (m==0){
            if (currentWeek - 1 > 0){
                currentWeek--;
                week = split[0] + "-" + currentWeek;
            }else {
                currentWeek = 1;
                Integer newYear = Integer.valueOf(split[0])-1;
                week = newYear + "-" + currentWeek;
            }
        }else {
            if (currentWeek + 1 <= 52){
                currentWeek++;
                week = split[0] + "-" + currentWeek;
            }else {
                currentWeek = 1;
                Integer newYear = Integer.valueOf(split[0])+1;
                week = newYear + "-" + currentWeek;
            }
        }

        return "redirect:/ranking/" + week;
    }


    @GetMapping("/ranking/detail/{id}")
    public String rankingDetailShow(@PathVariable Long id, Model model, @LoginUser SessionUser user){
        model.addAttribute("id", id);

        if (user!= null) {
            model.addAttribute("ranking", rankingService.findById(id));
            model.addAttribute("userName", user.getName());
            model.addAttribute("userId", user.getId());
        }

        return "ranking-detail";
    }

}
