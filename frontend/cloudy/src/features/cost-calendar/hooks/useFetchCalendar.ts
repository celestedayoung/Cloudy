import { fetchCalendar } from "../lib/fetchers";
import { CostCalendarResponse } from "../model/types";
import { useQuery } from "@tanstack/react-query";

export const useFetchCalendar = (containerId: string) => {
  return useQuery<CostCalendarResponse, Error>({
    queryKey: ["Calendar"],
    queryFn: () => fetchCalendar(containerId),
  });
};
